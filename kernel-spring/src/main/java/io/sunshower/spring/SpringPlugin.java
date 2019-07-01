package io.sunshower.spring;

import io.sunshower.api.ConfigurationSet;
import io.sunshower.api.Event;
import io.sunshower.api.Plugin;
import java.nio.file.Path;
import java.util.List;

public class SpringPlugin implements Plugin {

  final Object lock = new Object();

  private ClassLoader classLoader;
  private Object applicationContext;

  @Override
  public void stop(Object applicationContext) {}

  @Override
  public void start(Object applicationContext) {
    synchronized (lock) {
      this.applicationContext = applicationContext;
      this.classLoader = applicationContext.getClass().getClassLoader();
    }
    System.out.println("Starting..." + classLoader);
  }

  @Override
  public String getNativeId() {
    return null;
  }

  @Override
  public State getState() {
    return null;
  }

  @Override
  public Coordinate getCoordinate() {
    return null;
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
