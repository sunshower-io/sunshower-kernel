package io.sunshower.kernel.api;

public interface FulfillmentDefinition<T> extends LifecycleAware {

  String getMappedName();

  Class<T> getExtensionPoint();

  ExtensionCoordinate getCoordinate();
}
