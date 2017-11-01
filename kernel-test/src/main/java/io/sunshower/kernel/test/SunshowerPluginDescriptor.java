package io.sunshower.kernel.test;

import org.pf4j.DefaultPluginDescriptor;
import org.pf4j.PluginDescriptor;

import java.util.Map;

public class SunshowerPluginDescriptor extends DefaultPluginDescriptor {
    
    public static final String GROUP_ID_NAME = "group";
    public static final String ARTIFACT_ID_NAME = "artifact";
    public static final String VERSION_NAME = "version";
    public static final String DESCRIPTION_NAME = "version";
    public static final String PLUGIN_CLASS_NAME = "plugin-class";
    
    
    public SunshowerPluginDescriptor(Map load) {
        super(
                getRequired(GROUP_ID_NAME, load),
                get(DESCRIPTION_NAME, load),
                getRequired(PLUGIN_CLASS_NAME, load),
                get(VERSION_NAME, load),
                "*",
                "noone",
                "license"
        );
    }

    private static String getRequired(String pluginClassName, Map load) {
        return (String) load.get(pluginClassName);
    }

    private static String get(String value, Map load) {
        return (String) load.get(value);
    }
}
