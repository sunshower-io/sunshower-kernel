package io.sunshower.kernel.testplugins;

import io.sunshower.kernel.api.PluginManager;

import javax.annotation.Resource;
import javax.enterprise.inject.Produces;

public class Imports {
    
    @Produces
    @Resource(
            name = "java:global/kernel-test-war/WildflyPluginManager!io.sunshower.kernel.api.PluginManager"
    )
    private PluginManager pluginManager;

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public void setPluginManager(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }
}
