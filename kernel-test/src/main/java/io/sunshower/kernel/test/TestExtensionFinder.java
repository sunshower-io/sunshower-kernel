package io.sunshower.kernel.test;

import org.pf4j.DefaultExtensionFinder;
import org.pf4j.ExtensionFinder;
import org.pf4j.PluginManager;


public class TestExtensionFinder extends DefaultExtensionFinder implements ExtensionFinder {
    public TestExtensionFinder(PluginManager pluginManager) {
        super(pluginManager);
    }

}
