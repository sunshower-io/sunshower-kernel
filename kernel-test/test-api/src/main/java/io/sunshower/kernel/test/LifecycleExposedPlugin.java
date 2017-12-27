package io.sunshower.kernel.test;

import org.pf4j.PluginState;

public interface LifecycleExposedPlugin {
   
    PluginStatus getState();
}
