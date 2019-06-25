package io.sunshower.kernel.api;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public interface Plugin {


    /**
     * @return the state of this plugin
     */
    State getState();

    /**
     *
     * @return the globally-unique coordinate of this plugin
     */
    Coordinate getCoordinate();

    /**
     * Coordinates must be universally unique
     */


    /**
     * @return all of the extension points this plugin exports.  For instance, if this
     * plugin exports a service-type:
     *
     * interface SayHello {
     *     String sayHello();
     * }
     * then another plugin can fulfill that extension point
     */
    List<Class<?>> getExportedExtensionPoints();


    /**
     * This method is effectively the dual of @io.sunshower.kernel.api.Plugin#getExportedExtensionPoints
     * @param type
     * @param <T>
     * @return
     */
    <T> T getExtensionPoint(Class<T> type);

    /**
     * @param type
     * @param <T>
     * @return
     */
    <T> boolean exportsExtensionPoint(Class<T> type);


    /**
     * @return the underlying classloader for this plugin
     */
    ClassLoader getClassLoader();

    /**
     * @return the scratch location for this plugin
     */
    Path getPluginDirectory();


    /**
     * For plugins that declare a given, configurable extension point, this method
     * allows the plugin framework to instruct that plugin to reconfigure that extension point
     * @param type
     * @param configuration
     */
    void setConfiguration(Class<?> type, Object configuration);

    /**
     * Allow the plugin framework to retrieve a configuration for a given extension point
     * @param type
     * @return
     */
    Object getConfiguration(Class<?> type);


    /**
     * Retrieve all of the configurables for this plugin.  An example might be an
     * email-plugin that allows you to configure the email-server, set-from, port, protocol,
     * etc.
     * @return All the configurables exported by this plugin
     */
    List<ConfigurationSet> getConfigurables();

    /**
     * Retrieve all of the configurables under a given category.  For instance, if a plugin
     * has a user-component and an admin-component, then you might place all of the administrative
     * configurables under "admin" and all of the user components under "user"
     * @param category
     * @return
     */
    List<ConfigurationSet> getConfigurables(String category);

    /**
     *
     * @return the type of this plugin.  If it's top-level, then it may, for instance,
     * generate a menu-item
     */
    Type getType();


    /**
     * @return the context path of this plugin.  For instance, if this plugin is top-level
     * and generates a menu-item, navigating to that menu-item will display this plugin
     */
    String getContextPath();


    void dispatch(Event event, Event.Mode mode);

    @Getter
    @Setter
    class Coordinate {

        private String name;
        private String group;
        private String version;
    }


    /**
     * The plugin state
     */
    enum State {
        Running,
        Stopped,
        Failed
    }


    /**
     *
     */
    enum Type {
        Root,
        Extension
    }
}
