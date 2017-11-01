package io.sunshower.kernel;

import org.pf4j.DefaultExtensionFinder;
import org.pf4j.ExtensionFinder;
import org.pf4j.PluginManager;


public class SunshowerExtensionFinder extends DefaultExtensionFinder implements ExtensionFinder {
    public SunshowerExtensionFinder(PluginManager pluginManager) {
        super(pluginManager);
    }

}
