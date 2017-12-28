package io.sunshower.kernel;

import io.sunshower.kernel.api.KernelPluginException;
import io.sunshower.kernel.configuration.PluginConfiguration;
import org.pf4j.Plugin;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Field;

public class ConfigurationInjector {
    final Plugin                  instance;
    final Class<? extends Plugin> type;

    public ConfigurationInjector(Class<? extends Plugin> type, Plugin plugin) {
        this.type = type;
        this.instance = plugin;
    }
    
    public void read(PluginConfiguration configuration) {
        for (
                Class<?> current = type;
                current != null;
                current = current.getSuperclass()
                ) {
            Field[] fields = current.getDeclaredFields();
            for(Field field : fields) {
                final String propertyName;
                if(field.isAnnotationPresent(Inject.class)) {
                    if(!String.class.equals(field.getType())) {
                        throw new KernelPluginException("Field does not have type " +
                                "java.lang.String--cannot inject");

                    }
                    if(field.isAnnotationPresent(Named.class)) {
                        propertyName = field.getAnnotation(Named.class).value();
                    } else {
                        propertyName = field.getName();
                    }
                    field.setAccessible(true);
                    try {
                        Object o = field.get(instance);
                        if(o != null) {
                            configuration.addProperty(propertyName, (String) o);
                        }
                    } catch (IllegalAccessException e) {
                        throw new KernelPluginException(e);
                    }
                }
            }
        }
    }

    public void inject(PluginConfiguration configuration) {
        for (
                Class<?> current = type;
                current != null;
                current = current.getSuperclass()
                ) {
            Field[] fields = current.getDeclaredFields();
            for(Field field : fields) {
                final String propertyName;
                if(field.isAnnotationPresent(Inject.class)) {
                    if(!String.class.equals(field.getType())) {
                        throw new KernelPluginException("Field does not have type " +
                                "java.lang.String--cannot inject");
                        
                    }
                    if(field.isAnnotationPresent(Named.class)) {
                        propertyName = field.getAnnotation(Named.class).value();
                    } else {
                        propertyName = field.getName();
                    }
                    String property = configuration.getProperty(propertyName);
                    field.setAccessible(true);
                    try {
                        field.set(instance, property);
                    } catch (IllegalAccessException e) {
                        throw new KernelPluginException(e);
                    }
                }
            }
        }
    }
}
