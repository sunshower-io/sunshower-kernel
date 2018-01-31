package io.sunshower.kernel.api;


import java.util.List;

public interface PluginManager {


    List<ExtensionPointDefinition<?>> getExtensionPoints();

    <T> T resolve(Class<T> extension);

    <T> void register(Class<T> extensionPoint, T instance);


    <T> void register(Class<T> extensionPoint, T instance, ExtensionMetadata metadata);
}
