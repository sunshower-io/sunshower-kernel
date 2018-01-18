package io.sunshower.kernel.wildfly;

import io.sunshower.kernel.api.*;

import javax.ejb.Singleton;
import javax.enterprise.inject.spi.CDI;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class WildflyPluginManager implements PluginManager {
    
    private List<Object> extensions = new ArrayList<>();



    @Override
    @SuppressWarnings("unchecked")
    public <T> T resolve(Class<T> extension) {
        return (T) CDI.current().getBeanManager().getBeans(extension).iterator().next();
    }

    @Override
    public <T> void register(Class<T> extensionPoint, T instance) {
        extensions.add(instance);
    }
}
