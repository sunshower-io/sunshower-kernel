package io.sunshower.kernel.spring;

import io.sunshower.kernel.api.ExtensionCoordinate;
import io.sunshower.kernel.api.FulfillmentDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.jndi.JndiTemplate;

public class SpringFulfillmentDefinition<T> implements FulfillmentDefinition<T> {
  final String mappedName;
  final Class<T> extensionPoint;
  private final ExtensionCoordinate coordinate;

  public SpringFulfillmentDefinition(
      ExtensionCoordinate coordinate, String name, Class<T> extensionPoint) {
    this.mappedName = name;
    this.coordinate = coordinate;
    this.extensionPoint = extensionPoint;
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
  public String getMappedName() {
    return mappedName;
  }

  @Override
  public void start() {}

  @Override
  public void stop() {}
}
