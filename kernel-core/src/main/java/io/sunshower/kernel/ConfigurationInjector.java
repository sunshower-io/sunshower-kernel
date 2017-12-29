package io.sunshower.kernel;

import io.sunshower.kernel.api.KernelPluginException;
import io.sunshower.kernel.configuration.PluginConfiguration;
import org.pf4j.Plugin;
import org.pf4j.PluginException;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ConfigurationInjector {
    final Plugin                  instance;
    final Class<? extends Plugin> type;
    
    final Class<? extends Annotation> injectType;
    final Class<? extends Annotation> namedType;

    @SuppressWarnings("unchecked")
    public ConfigurationInjector(Class<? extends Plugin> type, Plugin plugin) {
        this.type = type;
        this.instance = plugin;
        try {
            ClassLoader pluginClassloader = plugin.getWrapper().getPluginClassLoader();
            injectType = (Class<? extends Annotation>) pluginClassloader.loadClass(Inject.class.getName());
            namedType = (Class<? extends Annotation>) pluginClassloader.loadClass(Named.class.getName());
        } catch(Exception ex) {
            throw new KernelPluginException(ex);
        }
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
                if(field.isAnnotationPresent(injectType)) {
                    if(PluginConfiguration.class.equals(field.getType())) {
                        continue;
                    }
                    if(!String.class.equals(field.getType())) {
                        throw new KernelPluginException("Field does not have type " +
                                "java.lang.String--cannot inject");

                    }
                    if(field.isAnnotationPresent(namedType)) {
                        propertyName = namedValue(field.getAnnotation(namedType));
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
                   
                    Class<?> type = field.getType();
                    
                    if(!(String.class.equals(type) || PluginConfiguration.class.equals(type))) {
                        throw new KernelPluginException("Field does not have type " +
                                "java.lang.String or PluginConfiguration--cannot inject");
                        
                    }
                    
                    if(String.class.equals(type)) {
                        if (field.isAnnotationPresent(namedType)) {
                            propertyName = namedValue(field.getAnnotation(namedType));
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
                    if(PluginConfiguration.class.equals(type)) {

                        field.setAccessible(true);
                        try {
                            field.set(instance, configuration);
                        } catch (IllegalAccessException e) {
                            throw new KernelPluginException(e);
                        }
                        
                    }
                }
            }
        }
    }

    private String namedValue(Annotation annotation) {
        try {
            return String.valueOf(namedType.getMethod("value").invoke(annotation));
        } catch (ReflectiveOperationException e) {
        }
        return "";
    }
}
