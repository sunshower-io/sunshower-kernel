package io.sunshower.kernel.spring;

import io.sunshower.kernel.api.*;
import org.springframework.context.ApplicationContext;

public class SpringExtensionPointDefinition<T> implements ExtensionPointDefinition<T> {
  private final Plugin plugin;
  private final ExtensionCoordinate coordinate;
  private final ApplicationContext applicationContext;

  public SpringExtensionPointDefinition(
      final ApplicationContext context, final ExtensionCoordinate coordinate, final Plugin plugin) {
    this.plugin = plugin;
    this.coordinate = coordinate;
    this.applicationContext = context;
  }

  @Override
  public ExtensionCoordinate getCoordinate() {
    return coordinate;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T load() {
    return (T) applicationContext.getBean(getMappedName());
  }

  @Override
  public String getMappedName() {
    return coordinate.getName();
  }
}
