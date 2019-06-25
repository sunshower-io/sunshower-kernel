package io.sunshower.kernel.api;


import java.io.File;
import java.nio.file.Path;
import java.util.List;

public interface PluginManager {


    /**
     * Retrieve the plugin at the given coordinate
     * @param coordinate
     * @return
     */
    Plugin lookup(Plugin.Coordinate coordinate) throws PluginNotFoundException;

    /**
     * @param event
     * @param mode
     * @param <T>
     */

    <T> void dispatchAll(Event<T> event, Event.Mode mode);

    /**
     *
     * @param event
     * @param mode
     * @param targets
     * @param <T>
     */
    <T> void dispatch(Event<T> event, Event.Mode mode, Plugin.Coordinate...targets);

    /**
     *
     * @return
     */
    List<Plugin> list();

    /**
     *
     * @param items
     * @return
     */

    List<Plugin> list(List<Plugin.Coordinate> items) throws PluginNotFoundException;


    /**
     *
     * @return
     */
    Path getPluginDirectory();

    /**
     *
     * @param coordinate
     * @return
     */
    Path getPluginDirectory(Plugin.Coordinate coordinate);
}
