package io.sunshower.spring;

import io.sunshower.api.*;
import java.nio.file.Path;
import java.util.List;
import javax.naming.NamingException;
import lombok.AllArgsConstructor;
import org.springframework.jndi.JndiTemplate;

@AllArgsConstructor
public class DelegatingPluginManager implements PluginManager {

  final JndiTemplate template;

  @Override
  public void waitForStartup() {
    pluginManager().waitForStartup();
  }

  @Override
  public int pendingDeploymentCount() {
    return pluginManager().pendingDeploymentCount();
  }

  @Override
  public Plugin lookup(PluginCoordinate coordinate) throws PluginNotFoundException {
    return pluginManager().lookup(coordinate);
  }

  @Override
  public <T> void dispatchAll(Event<T> event, Event.Mode mode) {
    pluginManager().dispatchAll(event, mode);
  }

  @Override
  public <T> void dispatch(Event<T> event, Event.Mode mode, PluginCoordinate... targets) {
    pluginManager().dispatch(event, mode, targets);
  }

  @Override
  public List<Plugin> list() {
    return pluginManager().list();
  }

  @Override
  public List<Plugin> list(List<PluginCoordinate> items) throws PluginNotFoundException {
    return pluginManager().list(items);
  }

  @Override
  public Path getPluginDirectory() {
    return pluginManager().getPluginDirectory();
  }

  @Override
  public Path getDataDirectory() {
    return pluginManager().getDataDirectory();
  }

  @Override
  public Path getDataDirectory(PluginCoordinate coordinate) {
    return pluginManager().getDataDirectory(coordinate);
  }

  @Override
  public Path getPluginDirectory(PluginCoordinate coordinate) {
    return pluginManager().getPluginDirectory(coordinate);
  }

  @Override
  public void rescan() {
    pluginManager().rescan();
  }

  @Override
  public void register(Plugin plugin) {
    throw new UnsupportedOperationException("Cannot register a plugin this way");
  }

  @Override
  public String getNativeId(PluginCoordinate coordinate) {
    return pluginManager().getNativeId(coordinate);
  }

  @Override
  public Plugin.State getState(PluginCoordinate coordinate) {
    return pluginManager().getState(coordinate);
  }

  private PluginManager pluginManager() {
    try {
      return template.lookup("java:global/sunshower/kernel/plugin-manager", PluginManager.class);
    } catch (NamingException e) {
      throw new PluginException("Failed to resolve plugin manager", e);
    }
  }
}
