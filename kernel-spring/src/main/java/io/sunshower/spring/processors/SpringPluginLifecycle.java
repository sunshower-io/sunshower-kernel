package io.sunshower.spring.processors;

import static java.lang.String.format;

import io.sunshower.api.PluginException;
import io.sunshower.api.PluginManager;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;

@Slf4j
public class SpringPluginLifecycle implements SmartLifecycle {
  private volatile boolean running = false;

  @Setter private static Class<?> entryPoint;
  @Setter private static PluginManager pluginManager;
  @Setter private static ClassLoader pluginClassloader;

  @Override
  public void start() {
    checkConfiguration();
    this.running = true;
  }

  @Override
  public void stop() {
    this.running = false;
  }

  @Override
  public boolean isRunning() {
    return running;
  }

  private void checkConfiguration() {
    require(SpringPluginLifecycle.entryPoint, "entry-point");
    require(SpringPluginLifecycle.pluginManager, "plugin manager");
    require(SpringPluginLifecycle.pluginClassloader, "plugin classloader");
  }

  private void require(Object o, String type) {
    log.info("Checking {}, role: {}...", o);
    if (o == null) {
      throw new PluginException(format("Cannot start plugin.  Reason: %s is not set", type));
    }
  }
}
