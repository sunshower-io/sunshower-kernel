package io.sunshower.kernel.testplugins;

import io.sunshower.kernel.api.ExtensionPoint;
import io.sunshower.kernel.api.PluginManager;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@ExtensionPoint(
        value = "theme",
        definition = Theme.class,
        extensionPoint = ThemeExtensionPoint.class
)
public class SampleTestPluginClass {
    
    @Inject
    private PluginManager pluginManager;
    
    @PostConstruct
    public void onStart() {
    }
    
    
}
