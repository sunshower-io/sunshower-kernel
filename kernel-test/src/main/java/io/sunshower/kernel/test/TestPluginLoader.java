package io.sunshower.kernel.test;

import org.pf4j.PluginClassLoader;
import org.pf4j.PluginDescriptor;
import org.pf4j.PluginLoader;
import org.pf4j.PluginManager;

import java.nio.file.Path;

public class TestPluginLoader implements PluginLoader {

    final ClassLoader parent;
    final PluginManager pluginManager;

    public TestPluginLoader(ClassLoader parent, PluginManager pluginManager) {
        this.parent = parent;
        this.pluginManager = pluginManager;
    }

    @Override
    public boolean isApplicable(Path pluginPath) {
        return false;
    }

    @Override
    public ClassLoader loadPlugin(Path pluginPath, PluginDescriptor pluginDescriptor) {
        return new PluginClassLoader(pluginManager, pluginDescriptor, parent);
    }
}
