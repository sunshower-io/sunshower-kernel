package io.sunshower.kernel.testplugins;

import io.sunshower.kernel.api.PluginManager;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class DefaultThemeManager implements ThemeManager {

  @Resource(
    name = "java:global/kernel-test-war/WildflyPluginManager!io.sunshower.kernel.api.PluginManager"
  )
  private PluginManager pluginManager;

  @Override
  public List<Theme> themes() {
    return null;
  }

  @Override
  public Theme getActiveTheme() {
    pluginManager.register(ThemeManager.class, this);
    return null;
  }

  public void postConstruct() {
    pluginManager.register(ThemeManager.class, this);
  }

  public PluginManager getPluginManager() {
    return pluginManager;
  }

  public void setPluginManager(PluginManager pluginManager) {
    this.pluginManager = pluginManager;
  }
}
