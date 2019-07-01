package io.sunshower.spring;

import io.sunshower.api.Plugin;
import io.sunshower.api.PluginManager;
import io.sunshower.spi.PluginLoader;

public class SpringPluginLoader implements PluginLoader {

  static final String key = "wildfly:kernel:plugins:spring";

  @Override
  public String getKey() {
    return key;
  }

  @Override
  public Plugin load(PluginManager manager, Object descriptor) {
    return null;
  }
}
