package io.sunshower.kernel.testplugins;

import io.sunshower.kernel.api.PluginManager;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.List;

@Startup
@Singleton
public class DefaultThemeManager implements ThemeManager {

  private PluginManager pluginManager;

  @Inject
  public DefaultThemeManager(PluginManager pluginManager) {
    this.pluginManager = pluginManager;
  }

  @Override
  public List<Theme> themes() {
    return null;
  }

  @Override
  public Theme getActiveTheme() {
    return new Theme() { };
  }

}
