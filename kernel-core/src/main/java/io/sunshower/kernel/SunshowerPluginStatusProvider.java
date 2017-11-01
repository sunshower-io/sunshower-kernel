package io.sunshower.kernel;

import org.pf4j.PluginStatusProvider;

public class SunshowerPluginStatusProvider implements PluginStatusProvider {
    @Override
    public boolean isPluginDisabled(String pluginId) {
        return false;
    }

    @Override
    public boolean disablePlugin(String pluginId) {
        return false;
    }

    @Override
    public boolean enablePlugin(String pluginId) {
        return false;
    }
}
