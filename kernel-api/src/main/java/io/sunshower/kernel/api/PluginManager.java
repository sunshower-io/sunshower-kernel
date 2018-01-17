package io.sunshower.kernel.api;

import java.util.SortedSet;

public interface PluginManager {

  <T extends Plugin> SortedSet<T> getPlugins();

  <T extends Plugin> SortedSet<T> getPlugins(PluginState state);

  <T extends Plugin> PluginRegistration register(Class<T> plugin);

  void setState(PluginRegistration plugin, PluginState state);
  
  PluginState getState(PluginRegistration pluginRegistration);
  
  PluginScanner getScanner(Location location);
  
}
