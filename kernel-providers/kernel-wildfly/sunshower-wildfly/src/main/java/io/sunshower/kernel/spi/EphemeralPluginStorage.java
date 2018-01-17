package io.sunshower.kernel.spi;

import io.sunshower.kernel.api.PluginStorage;

import javax.ejb.Stateless;

@Stateless(mappedName = PluginStorage.JNDI_NAME)
public class EphemeralPluginStorage implements PluginStorage {
    
    
    
}
