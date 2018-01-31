package io.sunshower.kernel.wildfly;

import io.sunshower.kernel.api.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Setter
@NoArgsConstructor
@Singleton
@EJB(name = Plugins.DefaultNamespaces.PLUGIN_MANAGER, beanInterface = PluginManager.class)
public class WildflyPluginManager implements PluginManager {

  @Inject @Default private PluginStorage pluginStorage;
  private final Map<Class<?>, CoordinateBinding> cache = new HashMap<>();

  @Override
  public List<ExtensionPointDefinition<?>> getExtensionPoints() {
    return pluginStorage.list();
  }

  @Override
  public <T> T resolve(Class<T> extension) {
    final ExtensionPointDefinition<T> definition = locate(extension);
    return definition.load(pluginStorage);
  }

  @Override
  public <T> void register(Class<T> extensionPoint, T instance) {
    final ExtensionPointDefinition definition =
        create(extensionPoint, instance, new EmptyMetadata());
    pluginStorage.save(definition);
  }

  @Override
  public <T> void register(Class<T> extensionPoint, T instance, ExtensionMetadata metadata) {
    final ExtensionPointDefinition definition = create(extensionPoint, instance, metadata);
    pluginStorage.save(definition);
  }

  @SuppressWarnings("unchecked")
  private <T> ExtensionPointDefinition create(
      Class<T> extensionPoint, T instance, ExtensionMetadata metadata) {
    CoordinateBinding coordinate = getCoordinate(extensionPoint);
    InMemoryExtensionPointDefinition<T> definition =
        new InMemoryExtensionPointDefinition<>(
            metadata, (Class<T>) coordinate.type, coordinate.coordinate, t -> instance);
    pluginStorage.save(definition);
    return definition;
  }

  private <T> ExtensionPointDefinition<T> locate(Class<T> extension) {
    CoordinateBinding coordinate = getCoordinate(extension);
    if (coordinate == null) {
      throw new NoSuchElementException(
          String.format("Extension with type %s not found", extension));
    }
    return pluginStorage.get(coordinate.coordinate);
  }

  private CoordinateBinding getCoordinate(Class<?> extensionPoint) {
    if (cache.containsKey(extensionPoint)) {
      return cache.get(extensionPoint);
    }
    for (Class<?> current = extensionPoint; current != null; current = current.getSuperclass()) {
      final ExtensionPoint definition = current.getAnnotation(ExtensionPoint.class);
      if (definition == null) {
        for (Class<?> ifaceType : current.getInterfaces()) {
          CoordinateBinding result = getCoordinate(ifaceType);
          if (result != null) {
            return result;
          }
        }
      } else {
        ExtensionCoordinate build =
            ExtensionCoordinate.builder()
                .name(definition.value())
                .namespace(definition.namespace())
                .group(definition.group())
                .build();
        return new CoordinateBinding(current, build);
      }
    }
    return null;
  }

  @AllArgsConstructor
  static class CoordinateBinding {
    final Class<?> type;
    final ExtensionCoordinate coordinate;
  }
}
