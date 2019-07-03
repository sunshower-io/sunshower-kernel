package io.sunshower.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import io.sunshower.api.Plugin;
import io.sunshower.api.PluginManager;
import io.sunshower.wildfly.SunshowerKernelPluginManager;
import io.sunshower.wildfly.extensions.AuthenticationExtension;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import lombok.SneakyThrows;
import lombok.val;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class SunshowerKernelPluginManagerSystemTest {

  @Deployment
  public static WebArchive deployment() {
    return Deployments.baseDeployment();
  }

  @Before
  public void waitForDeployment() throws NamingException, InterruptedException {

    while (pluginManager().list().isEmpty()) {
      Thread.sleep(100);
    }
  }

  @Test
  public void ensureDeploymentSizeIsImmediatelyCorrect() {
    assertThat(pluginManager().pendingDeploymentCount(), is(3));
  }

  @Test
  public void ensurePluginManagerIsInjected() {
    assertThat(pluginManager(), is(not(nullValue())));
  }

  @Test
  public void ensurePluginManagerDirectoryExists() {
    assertTrue(pluginManager().getPluginDirectory().toFile().exists());
  }

  @Test
  public void ensureIconCanBeRead() {
    val plugin = pluginManager().list().get(0);
    assertThat(plugin.getIcon(), is(not(nullValue())));
  }

  @Test
  public void ensureDataDirectoryExists() {
    assertThat(pluginManager().getDataDirectory().toFile().exists(), is(true));
  }

  @Test
  public void ensurePluginDataDirectoryExists() {
    val plugin = pluginManager().list().get(0);
    assertThat(
        pluginManager().getDataDirectory(plugin.getCoordinate()).toFile().exists(), is(true));
  }

  @Test
  public void ensureListingDeploymentsResultsInNamesBeingPopulated() {
    assertThat(pluginManager().list().size(), is(2));
  }

  @Test
  public void ensurePluginStatusIsOk() {
    val plugin = pluginManager().list().get(0);
    assertThat(plugin.getState(), is(Plugin.State.Running));
  }

  @Test
  @Ignore
  public void ensureExtensionPointsAreListed() {
    val plugin = pluginManager().list().get(0);
    assertThat(plugin.getExportedExtensionPoints(), is(not(nullValue())));
  }

  @Test
  public void ensureNativeIdReturnsCorrectResult() {
    val plugin = pluginManager().list().get(0);
    assertThat(plugin.getNativeId().contains(".war"), is(true));
    assertThat(plugin.getNativeId(), is(pluginManager().getNativeId(plugin.getCoordinate())));
  }

  @Test
  public void ensurePluginContainsExtensionPoint() {
    val plugin = pluginManager().list().get(0);
    plugin.exportsExtensionPoint(AuthenticationExtension.class);
  }

  @SneakyThrows
  private PluginManager pluginManager() {
    return InitialContext.doLookup(SunshowerKernelPluginManager.Name);
  }
}
