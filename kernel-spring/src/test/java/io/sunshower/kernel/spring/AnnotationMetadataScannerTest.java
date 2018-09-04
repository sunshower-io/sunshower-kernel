package io.sunshower.kernel.spring;

import io.sunshower.kernel.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import javax.naming.NamingException;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AnnotationMetadataScannerTest.Ctx.class)
class AnnotationMetadataScannerTest {

  @Inject private Plugin plugin;

  @Inject private JndiTemplate template;

  @Inject private ApplicationContext context;

  @Test
  void ensureScanningExtensionPointsProducesListOfCorrectSize() {
    assertThat(plugin.getExtensionPoints().size(), is(2));
  }

  @Test
  void ensureScanningFullfillmentsProducesListOfCorrectSize() {
    assertThat(plugin.getExtensionFulfillments().size(), is(2));
  }

  @Test
  void ensureFulfillmentDefinitionIsResolvableByTypeAndName() {
    final FulfillmentDefinition<TestFulfillment> testFulfillmentImpl =
        plugin.getFulfillmentDefinition(TestFulfillment.class, "testFulfillmentImpl");
    assertThat(testFulfillmentImpl, is(not(nullValue())));
  }

  @Test
  void ensureFulfillmentsAreConsistentIrrespectiveOfDeclarationSite() {
    final FulfillmentDefinition<TestFulfillment> testFulfillmentImpl =
        plugin.getFulfillmentDefinition(TestFulfillment.class, "testFulfillmentImpl");
    final FulfillmentDefinition<TestExtensionPoint> exampleExtensionPoint =
        plugin.getFulfillmentDefinition(TestExtensionPoint.class, "extensionPointExample");
    assertThat(exampleExtensionPoint.getExtensionPoint(), is(equalTo(TestExtensionPoint.class)));
    assertThat(testFulfillmentImpl.getExtensionPoint(), is(equalTo(TestFulfillment.class)));
  }

  @Test
  void ensureClassLoaderIsNotNull() {
    assertThat(plugin.getPluginClassLoader(), is(not(nullValue())));
  }

  @Test
  void ensureExportingAllWorks() throws NamingException {
    plugin.exportAll();
    verify(template, times(3)).rebind(Mockito.any(String.class), Mockito.any(Object.class));
  }

  @Test
  void ensureFulfillingByTypeOnAnnotatedClassWorks() {
    assertNotNull(plugin.fulfill(TestFulfillment.class));
  }

  @Test
  void ensureFulfillingByTypeOnAnnotatedMethodWorks() {
    assertNotNull(plugin.fulfill(TestExtensionPoint.class));
  }

  @Test
  void ensureLocatingFulfillmentByTypeAndNameWorks() {
    assertNotNull(plugin.fulfill(TestExtensionPoint.class, "extensionPointExample"));
  }

  @Configuration
  @ComponentScan(basePackages = "io.sunshower.kernel.spring")
  public static class Ctx {

    @Bean
    public PluginExporter pluginExporter(JndiTemplate template, NamingStrategy strategy) {
      return new JNDIPluginExporter<>(template, strategy);
    }

    @Bean
    public JndiTemplate jndiTemplate() {
      return mock(JndiTemplate.class);
    }

    @Bean
    public SpringPlugin testPlugin(
        ApplicationContext context, NamingStrategy strategy, PluginExporter exporter) {
      return new SpringPlugin(
          new ExtensionCoordinate("group", "namespace", "name", "1.0.0-snapshot"), context, strategy, exporter);
    }

    @Bean
    public NamingStrategy jndiNamingStrategy() {
      return new JNDINamingStrategy();
    }

    @Bean
    @Fulfillment(fulfillmentType = TestExtensionPoint.class)
    public TestExtensionPoint extensionPointExample() {
      return new TestExtensionPointServices();
    }

    @Bean
    @ExtensionPoint(name = "ignite")
    public String igniteBean() {
      return "Hello";
    }
  }
}
