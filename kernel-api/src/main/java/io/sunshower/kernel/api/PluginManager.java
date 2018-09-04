package io.sunshower.kernel.api;

import java.util.List;

public interface PluginManager {

  void register(Plugin plugin);

  Plugin getPlugin(ExtensionCoordinate coordinate);

  List<Plugin> getPlugins();

  void stopPlugin(ExtensionCoordinate coordinate);

  void startPlugin(ExtensionCoordinate coordinate);
}
