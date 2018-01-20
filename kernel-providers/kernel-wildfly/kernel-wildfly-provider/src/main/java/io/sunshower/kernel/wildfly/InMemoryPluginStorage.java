package io.sunshower.kernel.wildfly;

import io.sunshower.kernel.api.ExtensionCoordinate;
import io.sunshower.kernel.api.ExtensionPointDefinition;
import io.sunshower.kernel.api.PluginStorage;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Setter
@Singleton
@NoArgsConstructor
public class InMemoryPluginStorage implements PluginStorage {

  private final Map<ExtensionCoordinate, ExtensionPointDefinition<?>> cache = new LinkedHashMap<>();

  @Override
  public void save(ExtensionPointDefinition extensionPoint) {
    cache.put(extensionPoint.getCoordinate(), extensionPoint);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> ExtensionPointDefinition<T> get(ExtensionCoordinate coordinate) {
    return (ExtensionPointDefinition<T>) cache.get(coordinate);
  }

  @Override
  public List<ExtensionPointDefinition<?>> list() {
    return new ArrayList<>(cache.values());
  }
}
