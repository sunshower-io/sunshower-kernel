package io.sunshower.kernel.wildfly;

import io.sunshower.kernel.api.ExtensionCoordinate;
import io.sunshower.kernel.api.ExtensionMetadata;
import io.sunshower.kernel.api.ExtensionPointDefinition;
import io.sunshower.kernel.api.PluginStorage;

import java.util.function.Function;

public class InMemoryExtensionPointDefinition<T> implements ExtensionPointDefinition<T> {

  final Class<T> extensionPoint;
  final ExtensionCoordinate coordinate;
  final Function<PluginStorage, T> instanceProvider;
  private final ExtensionMetadata extensionMetadata;

  public InMemoryExtensionPointDefinition(
      ExtensionMetadata metadata,
      Class<T> type,
      ExtensionCoordinate coordinate,
      Function<PluginStorage, T> instanceProvider) {
    this.extensionPoint = type;
    this.extensionMetadata = metadata;
    this.coordinate = coordinate;
    this.instanceProvider = instanceProvider;
  }

  @Override
  public Class<T> getExtensionPoint() {
    return extensionPoint;
  }

  @Override
  public ExtensionCoordinate getCoordinate() {
    return coordinate;
  }

  @Override
  public T load(PluginStorage storage) {
    return instanceProvider.apply(storage);
  }

  @Override
  public ExtensionMetadata getMetadata() {
      return extensionMetadata;
  }
}
