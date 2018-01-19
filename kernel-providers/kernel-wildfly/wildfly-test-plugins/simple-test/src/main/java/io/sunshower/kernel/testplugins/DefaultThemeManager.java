package io.sunshower.kernel.testplugins;

import io.sunshower.kernel.api.PluginManager;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.*;

@Startup
@Singleton
public class DefaultThemeManager implements ThemeManager {

  private Set<Theme> themes;
  private PluginManager pluginManager;

  @Inject
  public DefaultThemeManager(PluginManager pluginManager) {
    this.themes = new LinkedHashSet<>();
    this.pluginManager = pluginManager;
  }

  @Override
  public void register(Theme theme) {
      themes.add(theme);
  }

  @Override
  public List<Theme> themes() {
      return new ArrayList<>(themes);
  }

  @Override
  public Theme getActiveTheme() {
      return themes.iterator().next();
  }
}
