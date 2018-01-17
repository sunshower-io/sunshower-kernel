package io.sunshower.kernel.api;


import java.util.List;

public interface PluginManager {
    
    <T> List<T> resolve(Class<T> extension);

    <T> void register(Class<T> extensionPoint, T instance);
}
