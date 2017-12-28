package io.sunshower.kernel;

import org.pf4j.DefaultPluginDescriptor;

import java.util.Map;

public class YamlPluginDescriptor extends DefaultPluginDescriptor {
    
    public static final String GROUP_ID_NAME = "group";
    public static final String ARTIFACT_ID_NAME = "artifact";
    public static final String VERSION_NAME = "version";
    public static final String DESCRIPTION_NAME = "version";
    public static final String PLUGIN_CLASS_NAME = "plugin-class";
    public static final String DEPENDENCIES_NAME = "dependencies";
    
    
    public YamlPluginDescriptor(Map load) {
        super(
                getRequired(GROUP_ID_NAME, load),
                get(DESCRIPTION_NAME, load),
                getRequired(PLUGIN_CLASS_NAME, load),
                get(VERSION_NAME, load),
                "*",
                "noone",
                "license"
        );

        String s = get(DEPENDENCIES_NAME, load);
        if(s != null) {
            setDependencies(s);
        }
    }

    private static String getRequired(String pluginClassName, Map load) {
        return (String) load.get(pluginClassName);
    }

    private static String get(String value, Map load) {
        return (String) load.get(value);
    }
}
