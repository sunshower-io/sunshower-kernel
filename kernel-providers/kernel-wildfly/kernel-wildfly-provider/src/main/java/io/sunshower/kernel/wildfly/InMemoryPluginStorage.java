package io.sunshower.kernel.wildfly;

import io.sunshower.kernel.api.ExtensionCoordinate;
import io.sunshower.kernel.api.ExtensionPointDefinition;
import io.sunshower.kernel.api.PluginStorage;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.ejb.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Setter
@Singleton
@NoArgsConstructor
public class InMemoryPluginStorage implements PluginStorage {

  private final Map<ExtensionCoordinate, ExtensionPointDefinition<?>> cache =
      new ConcurrentHashMap<>();

  @Override
  public void save(ExtensionPointDefinition extensionPoint) {
    cache.put(extensionPoint.getCoordinate(), extensionPoint);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> ExtensionPointDefinition<T> get(ExtensionCoordinate coordinate) {
    return (ExtensionPointDefinition<T>) cache.get(coordinate);
  }
}
