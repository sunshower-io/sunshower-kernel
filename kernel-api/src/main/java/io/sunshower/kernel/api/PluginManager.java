package io.sunshower.kernel.api;



public interface PluginManager {

    <T> T resolve(Class<T> extension);

    <T> void register(Class<T> extensionPoint, T instance);
}
