package io.sunshower.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import io.sunshower.api.PluginManager;
import io.sunshower.wildfly.SunshowerKernelPluginManager;
import javax.annotation.Resource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class SunshowerKernelPluginManagerSystemTest {
  @Deployment
  public static WebArchive deployment() {
    return Deployments.baseDeployment();
  }

  @Resource(name = SunshowerKernelPluginManager.Name)
  private PluginManager pluginManager;

  @Test
  public void ensurePluginManagerIsInjected() {
    assertThat(pluginManager, is(not(nullValue())));
  }

  @Test
  public void ensurePluginManagerDirectoryExists() {
    assertTrue(pluginManager.getPluginDirectory().toFile().exists());
  }

  @Test
  public void ensureListingDeploymentsResultsInNamesBeingPopulated() {
    pluginManager.list();
  }
}
