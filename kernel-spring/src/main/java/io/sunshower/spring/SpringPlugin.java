package io.sunshower.spring;

import io.sunshower.api.*;
import io.sunshower.spi.PluginRegistrar;
import java.nio.file.Path;
import java.security.ProtectionDomain;
import java.util.List;

public class SpringPlugin implements Plugin {

  private final ClassLoader classLoader;
  private final PluginRegistrar registrar;
  private final PluginCoordinate coordinate;
  private final ProtectionDomain protectionDomain;
  private final LifecycleManager applicationContext;

  public SpringPlugin(
      ClassLoader classLoader,
      PluginCoordinate coordinate,
      PluginRegistrar pluginRegistrar,
      ProtectionDomain protectionDomain,
      LifecycleManager applicationContext) {
    this.classLoader = classLoader;
    this.coordinate = coordinate;
    this.registrar = pluginRegistrar;
    this.protectionDomain = protectionDomain;
    this.applicationContext = applicationContext;
  }

  @Override
  public PluginRegistrar getRegistrar() {
    return registrar;
  }

  @Override
  public ProtectionDomain getProtectionDomain() {
    return protectionDomain;
  }

  @Override
  public void stop(Object applicationContext) {}

  @Override
  public void start(Object applicationContext) {}

  @Override
  public String getNativeId() {
    return null;
  }

  @Override
  public State getState() {
    return null;
  }

  @Override
  public PluginCoordinate getCoordinate() {
    return coordinate;
  }

  @Override
  public List<Class<?>> getExportedExtensionPoints() {
    return null;
  }

  @Override
  public <T> T getExtensionPoint(Class<T> type) {
    return null;
  }

  @Override
  public <T> boolean exportsExtensionPoint(Class<T> type) {
    return false;
  }

  @Override
  public ClassLoader getClassLoader() {
    return null;
  }

  @Override
  public Path getPluginDirectory() {
    return null;
  }

  @Override
  public void setConfiguration(Class<?> type, Object configuration) {}

  @Override
  public Object getConfiguration(Class<?> type) {
    return null;
  }

  @Override
  public List<ConfigurationSet> getConfigurables() {
    return null;
  }

  @Override
  public List<ConfigurationSet> getConfigurables(String category) {
    return null;
  }

  @Override
  public Type getType() {
    return null;
  }

  @Override
  public String getContextPath() {
    return null;
  }

  @Override
  public void dispatch(Event event, Event.Mode mode) {}
}
