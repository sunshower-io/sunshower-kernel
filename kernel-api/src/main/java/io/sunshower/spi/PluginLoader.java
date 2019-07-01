package io.sunshower.spi;

import io.sunshower.api.Plugin;
import io.sunshower.api.PluginManager;

public interface PluginLoader<T> {
  String getKey();

  Plugin load(PluginManager manager, T descriptor);
}
