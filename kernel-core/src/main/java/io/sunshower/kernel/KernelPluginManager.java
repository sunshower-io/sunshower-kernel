package io.sunshower.kernel;

import io.sunshower.kernel.api.OnStop;
import io.sunshower.kernel.api.OnUnload;
import org.pf4j.*;
import org.pf4j.Plugin;

import java.nio.file.Path;
import java.util.logging.Logger;

import static io.sunshower.kernel.Plugins.fire;

public class KernelPluginManager extends DefaultPluginManager {

    static final Logger logger = Logger.getLogger(KernelPluginManager.class.getName());

    public KernelPluginManager(Path pluginsRoot) {
        super(pluginsRoot);
    }

    @Override
    protected PluginLoader createPluginLoader() {
        return new CompoundPluginLoader()
                .add(new KernelPluginLoader(this, pluginClasspath))
                .add(new DefaultPluginLoader(this, pluginClasspath))
                .add(new JarPluginLoader(this));
    }

    @Override
    protected PluginDescriptorFinder createPluginDescriptorFinder() {
        return new KernelPluginDescriptorFinder();
    }

    @Override
    protected PluginRepository createPluginRepository() {
        return new DirectoryPluginRepository(getPluginsRoot().toFile());
    }

    @Override
    protected PluginFactory createPluginFactory() {
        return new AnnotationAwarePluginFactory();
    }


    @Override
    protected void beforeStop(PluginWrapper pluginWrapper) {
        Plugin plugin = pluginWrapper.getPlugin();
        Class<? extends Plugin> pluginClass = plugin.getClass();
        fire(pluginClass, plugin, OnStop.class);
    }

    @Override
    protected void beforeUnload(PluginWrapper pluginWrapper) {
        Plugin plugin = pluginWrapper.getPlugin();
        Class<? extends Plugin> pluginClass = plugin.getClass();
        fire(pluginClass, plugin, OnUnload.class);
    }
}
