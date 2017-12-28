package io.sunshower.kernel;

import org.junit.jupiter.api.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class ConfigurationInjectorTest extends KernelTestCase {

    @Test
    public void ensurePluginDescriptorWriteWritesNameFromFieldValue() {
        plugin.setHelloName("coolio");
        configurationInjector.read(configuration);
        assertThat(configuration.getProperty("hello-name"), is("coolio"));
    }
    
    @Test
    public void ensurePluginDescriptorWriteWritesNameFromFieldName() {
        plugin.setName("frapper");
        configurationInjector.read(configuration);
        assertThat(configuration.getProperty("name"), is("frapper"));
    }
    
    @Test
    public void ensurePluginDescriptorReadSetsPropertyNameFromName() {
        configuration.addProperty("hello-name", "coolio");
        configurationInjector.inject(configuration);
        assertThat(plugin.getHelloName(), is(equalTo("coolio")));
        
    }

    @Test
    public void ensurePluginDescriptorReadSetsPropertyFromValue() {
        configuration.addProperty("name", "josiah");
        configurationInjector.inject(configuration);
        assertThat(plugin.getName(), is(equalTo("josiah")));
    }

}