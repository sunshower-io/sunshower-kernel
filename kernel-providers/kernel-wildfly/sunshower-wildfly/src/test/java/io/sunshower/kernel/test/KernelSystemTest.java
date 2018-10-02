package io.sunshower.kernel.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import io.sunshower.kernel.api.ExtensionCoordinate;
import io.sunshower.kernel.api.FulfillmentDefinition;
import io.sunshower.kernel.api.Plugin;
import io.sunshower.kernel.api.PluginManager;
import io.sunshower.test.common.TestClasspath;
import java.io.File;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class KernelSystemTest {

  static File file(String path) {
    return new File(TestClasspath.buildDir().getParentFile(), path);
  }

  @Resource(name = "java:global/sunshower/kernel/plugin-manager")
  private PluginManager pluginManager;

  @Inject private ServletContext servletContext;

  @Resource(name = "java:global/sunshower/kernel/plugin-manager")
  private PluginManager globalPluginManager;

  @Deployment
  public static WebArchive webArchive() {
    return ShrinkWrap.create(WebArchive.class, "kernel-test-war3.war")
        .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
        .addAsWebInfResource(file("src/test/webapp/WEB-INF/jboss-deployment-structure.xml"))
        .addClass(KernelSystemTest.class)
        .addClass(TestClasspath.class);
  }

  @Test
  public void ensurePluginsWork() {
    assertThat(globalPluginManager.getPlugins().size(), is(1));
  }

  @Test
  public void ensureFulfillmentExists() {
    final Plugin plugin =
        globalPluginManager.getPlugin(new ExtensionCoordinate("test", "test", "test", "test"));

    assertTrue(plugin.getExtensionFulfillments().size() > 0);
    for (FulfillmentDefinition<?> def : plugin.getExtensionFulfillments()) {
      assertThat(plugin.fulfill(def), is(not(nullValue())));
    }
  }
}
