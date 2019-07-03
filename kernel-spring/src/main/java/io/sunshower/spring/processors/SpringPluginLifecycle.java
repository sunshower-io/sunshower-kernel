package io.sunshower.spring.processors;

import static java.lang.String.format;

import io.sunshower.api.*;
import io.sunshower.spi.PluginRegistrar;
import io.sunshower.spring.SpringPlugin;
import javax.servlet.ServletContext;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;

@Slf4j
public class SpringPluginLifecycle
    implements SmartLifecycle, ApplicationContextAware, LifecycleManager, PluginRegistrar {
  /** Static fields */
  @Setter private Class<?> entryPoint;

  @Setter private ServletContext servletContext;
  @Setter private PluginCoordinate coordinate;
  @Setter private PluginManager pluginManager;
  @Setter private ClassLoader pluginClassloader;

  // Don't use this--for java ServiceLoader
  public SpringPluginLifecycle() {}

  public SpringPluginLifecycle(
      Class<?> entryPoint,
      ServletContext servletContext,
      PluginCoordinate coordinate,
      PluginManager pluginManager,
      ClassLoader pluginClassloader) {
    this.entryPoint = entryPoint;
    this.servletContext = servletContext;
    this.coordinate = coordinate;
    this.pluginManager = pluginManager;
    this.pluginClassloader = pluginClassloader;
  }

  /** Members (non-injected) */
  private ApplicationContext context;

  private volatile boolean running = false;

  @Override
  public void start() {
    checkConfiguration();
    val plugin = createPlugin();
    pluginManager.register(plugin);
    this.running = true;
  }

  @Override
  public void stop() {
    this.running = false;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T unwrap(Class<T> type) {
    if (!ApplicationContext.class.isAssignableFrom(type)) {
      throw new IllegalArgumentException("Can't unwrap " + type);
    }
    return (T) context;
  }

  @Override
  public boolean isRunning() {
    return running;
  }

  private Plugin createPlugin() {
    return new SpringPlugin(
        servletContext,
        pluginManager,
        pluginClassloader,
        coordinate,
        this,
        SpringPluginLifecycle.class.getProtectionDomain(),
        this);
  }

  private void checkConfiguration() {
    require(entryPoint, "entry-point");
    require(pluginManager, "plugin manager");
    require(pluginClassloader, "plugin classloader");
    require(coordinate, "plugin coordinate");
  }

  private void require(Object o, String type) {
    log.info("Checking {}, role: {}...", o);
    if (o == null) {
      throw new PluginException(format("Cannot start plugin.  Reason: %s is not set", type));
    }
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.context = applicationContext;
  }
}
