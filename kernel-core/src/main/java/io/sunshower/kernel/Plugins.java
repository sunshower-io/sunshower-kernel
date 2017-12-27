package io.sunshower.kernel;

import io.sunshower.kernel.api.KernelPluginException;
import org.pf4j.PluginState;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;


public class Plugins {
    
    public static boolean isEnabled(PluginWrapper p) {
        return !isDisabled(p);
    }
    
    public static boolean isDisabled(PluginWrapper p) {
        Objects.requireNonNull(p);
        return p.getPluginState() == PluginState.DISABLED;
        
    }



    public static void fire(Class<?> pluginClass, Plugin plugin, Class<? extends Annotation> annotation) {
        for (
                Class<?> current = pluginClass;
                current != null;
                current = current.getSuperclass()) {
            for (Method m : current.getDeclaredMethods()) {
                if (m.isAnnotationPresent(annotation)) {
                    if (!Modifier.isPublic(m.getModifiers())) {
                        throw new KernelPluginException(
                                createException(pluginClass, current, m)
                        );
                    } else {
                        try {
                            m.invoke(plugin);
                        } catch (Exception e) {
                            throw new KernelPluginException(e);
                        }
                    }
                }
            }
        }
    }

    private static String createException(Class<?> pluginClass, Class<?> current, Method m) {
        return String.format("Error:  method '%s' on plugin of type: " +
                "'%s' (Actual type: '%s') is not public.  " +
                "Cannot load", pluginClass, current, m.getName()
        );
    }
}
