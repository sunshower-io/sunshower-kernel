package io.sunshower.kernel.test;

import org.pf4j.PluginDescriptor;
import org.pf4j.PluginFactory;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;

import java.nio.file.Path;

public class DefaultPluginWrapper extends PluginWrapper {
    public DefaultPluginWrapper(PluginManager pluginManager, PluginDescriptor descriptor, Path pluginPath, ClassLoader pluginClassLoader) {
        super(pluginManager, descriptor, pluginPath, pluginClassLoader);
    }
    
    public void setPluginFactory(PluginFactory factory) {
        super.setPluginFactory(factory);
    }
}
