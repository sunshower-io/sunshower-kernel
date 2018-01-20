package io.sunshower.kernel.api;

public interface PluginStorage {

  String JNDI_NAME = "java:sunshower/kernel/plugins/storage";

  void save(ExtensionPointDefinition extensionPoint);

  <T> ExtensionPointDefinition<T> get(ExtensionCoordinate coordinate);
}
