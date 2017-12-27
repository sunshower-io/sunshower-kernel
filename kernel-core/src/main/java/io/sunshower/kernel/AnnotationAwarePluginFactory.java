package io.sunshower.kernel;

import io.sunshower.kernel.api.OnLoad;
import io.sunshower.kernel.api.OnStart;
import org.pf4j.PluginFactory;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static io.sunshower.kernel.Plugins.fire;

public class AnnotationAwarePluginFactory implements PluginFactory {


    private static final Logger log = LoggerFactory.getLogger(AnnotationAwarePluginFactory.class);

    @Override
    public Plugin create(final PluginWrapper pluginWrapper) {
        String pluginClassName = pluginWrapper.getDescriptor().getPluginClass();
        log.debug("Create instance for plugin '{}'", pluginClassName);

        Class<?> pluginClass;
        try {
            pluginClass = pluginWrapper.getPluginClassLoader().loadClass(pluginClassName);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
            return null;
        }

        // once we have the class, we can do some checks on it to ensure
        // that it is a valid implementation of a plugin.
        int modifiers = pluginClass.getModifiers();
        if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers)
                || (!Plugin.class.isAssignableFrom(pluginClass))) {
            log.error("The plugin class '{}' is not valid", pluginClassName);
            return null;
        }

        // create the plugin instance
        try {
            Constructor<?> constructor = pluginClass.getConstructor(PluginWrapper.class);
            final Plugin   plugin      = (Plugin) constructor.newInstance(pluginWrapper);
            fire(pluginClass, plugin, OnLoad.class);
            fire(pluginClass, plugin, OnStart.class);
            return plugin;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
