package io.sunshower.kernel.testplugins2;

import io.sunshower.kernel.api.*;
import io.sunshower.kernel.spring.JNDINamingStrategy;
import io.sunshower.kernel.spring.JNDIPluginExporter;
import io.sunshower.kernel.spring.SpringPlugin;
import javax.naming.NamingException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiTemplate;

@Configuration
public class TestPluginConfig {

  @Bean
  public ExtensionCoordinate coordinate() {
    return new ExtensionCoordinate("test", "test", "test", "test");
  }

  @Bean
  public NamingStrategy namingStrategy() {
    return new JNDINamingStrategy();
  }

  @Bean
  public PluginExporter pluginExporter(JndiTemplate template, NamingStrategy strategy) {
    return new JNDIPluginExporter(template, strategy);
  }

  @Bean
  public JndiTemplate template() {
    return new JndiTemplate();
  }

  @Bean
  public Plugin testPlugin1(
      ExtensionCoordinate coordinate,
      NamingStrategy strategy,
      ApplicationContext context,
      PluginExporter exporter,
      JndiTemplate template)
      throws NamingException {
    final Plugin plugin = new SpringPlugin(coordinate, context, strategy, exporter);
    final PluginManager manager =
        template.lookup("java:global/sunshower/kernel/plugin-manager", PluginManager.class);
    manager.register(plugin);
    return plugin;
  }

  @Bean
  @Fulfillment(fulfillmentType = TestFulfillment.class)
  public TestFulfillment testFulfillment() {
    return new DefaultTestFulfillment();
  }
}
