package io.sunshower.spring.processors;

import io.sunshower.api.PluginManager;
import lombok.Setter;
import org.springframework.context.SmartLifecycle;

public class SpringPluginLifecycle implements SmartLifecycle {
  private volatile boolean running = false;

  @Setter private static Class<?> entryPoint;
  @Setter private static PluginManager pluginManager;
  @Setter private static ClassLoader pluginClassloader;

  @Override
  public void start() {
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
}
