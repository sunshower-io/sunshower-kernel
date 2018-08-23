package io.sunshower.kernel.api;

public interface ExtensionPointDefinition<T> {

  ExtensionCoordinate getCoordinate();

  T load();

  String getMappedName();
}
