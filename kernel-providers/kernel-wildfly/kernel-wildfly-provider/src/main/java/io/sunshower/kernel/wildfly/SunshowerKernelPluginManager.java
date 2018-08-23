package io.sunshower.kernel.wildfly;

import io.sunshower.kernel.api.ExtensionCoordinate;
import io.sunshower.kernel.api.Plugin;
import io.sunshower.kernel.api.PluginManager;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@EJB(name = "java:global/sunshower/kernel/plugin-manager", beanInterface = PluginManager.class)
public class SunshowerKernelPluginManager implements PluginManager {



  private final Map<ExtensionCoordinate, Plugin> plugins = new ConcurrentHashMap<>();

  @Override
  public void register(Plugin plugin) {
    plugins.put(
        new ExtensionCoordinate(
            plugin.getGroup(), plugin.getNamespace(), plugin.getName(), plugin.getVersion()),
        plugin);
  }

  @Override
  public Plugin getPlugin(ExtensionCoordinate coordinate) {
    return plugins.get(coordinate);
  }

  @Override
  public List<Plugin> getPlugins() {
    return new ArrayList<>(plugins.values());
  }

  @Override
  public void stopPlugin(ExtensionCoordinate coordinate) {
    getPlugin(coordinate).stop();
  }

  @Override
  public void startPlugin(ExtensionCoordinate coordinate) {
    getPlugin(coordinate).start();
  }
}
