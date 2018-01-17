package io.sunshower.kernel.wildfly;

import io.sunshower.kernel.api.*;

import java.util.SortedSet;

public class WildflyPluginManager implements PluginManager {
    
    private final PluginStorage storage;
    
    public WildflyPluginManager(PluginStorage storage) {
        this.storage = storage;
    }
    
}
