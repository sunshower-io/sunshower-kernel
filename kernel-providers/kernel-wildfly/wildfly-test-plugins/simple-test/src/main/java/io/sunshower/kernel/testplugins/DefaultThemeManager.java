package io.sunshower.kernel.testplugins;

import io.sunshower.kernel.api.PluginManager;
import io.sunshower.kernel.api.ServletExtensionMetadata;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Startup
@Singleton
public class DefaultThemeManager implements ThemeManager {

  private Set<Theme> themes;
  private PluginManager pluginManager;

  @Inject private DeploymentDetector deploymentDetector;

  @Inject
  public DefaultThemeManager(PluginManager pluginManager) {
    this.themes = new LinkedHashSet<>();
    this.pluginManager = pluginManager;
  }

  @PostConstruct
  public void registerAll() {
    register(new SampleTheme());
    pluginManager.register(
        ThemeManager.class,
        this,
        new DeploymentDetector());
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
