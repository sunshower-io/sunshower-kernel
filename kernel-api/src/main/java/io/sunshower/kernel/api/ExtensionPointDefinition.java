package io.sunshower.kernel.api;

public interface ExtensionPointDefinition<T> {
    
    
    Class<T> getExtensionPoint();
    
    ExtensionCoordinate getCoordinate();
    
    T load(PluginStorage storage);

    ExtensionMetadata getMetadata();
}
