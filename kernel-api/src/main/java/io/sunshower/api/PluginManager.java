package io.sunshower.api;

import java.nio.file.Path;
import java.util.List;

public interface PluginManager {

  /** Wait until all pending deployments have been deployed */
  void waitForStartup();

  /** @return the number of plugins waiting to be deployed */
  int pendingDeploymentCount();

  /**
   * Retrieve the plugin at the given coordinate
   *
   * @param coordinate
   * @return
   */
  Plugin lookup(PluginCoordinate coordinate) throws PluginNotFoundException;

  /**
   * @param event
   * @param mode
   * @param <T>
   */
  <T> void dispatchAll(Event<T> event, Event.Mode mode);

  /**
   * @param event
   * @param mode
   * @param targets
   * @param <T>
   */
  <T> void dispatch(Event<T> event, Event.Mode mode, PluginCoordinate... targets);

  /** @return */
  List<Plugin> list();

  /**
   * @param items
   * @return
   */
  List<Plugin> list(List<PluginCoordinate> items) throws PluginNotFoundException;

  /** @return */
  Path getPluginDirectory();

  /** @return the data directory for sunshower::kernel */
  Path getDataDirectory();

  /**
   * @param coordinate the plugin to retrieve the data directory from
   * @return the data directory for the given plugin
   */
  Path getDataDirectory(PluginCoordinate coordinate);

  /**
   * @param coordinate
   * @return
   */
  Path getPluginDirectory(PluginCoordinate coordinate);

  /** Trigger rescan for plugin */
  void rescan();

  void register(Plugin plugin);

  String getNativeId(PluginCoordinate coordinate);

  Plugin.State getState(PluginCoordinate coordinate);
}
