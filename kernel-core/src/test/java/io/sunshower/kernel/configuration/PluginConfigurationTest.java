package io.sunshower.kernel.configuration;

import io.sunshower.kernel.KernelTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class PluginConfigurationTest extends KernelTestCase {
   

    @BeforeEach
    public void setUp() {
        super.setUp();
    }
    
    
    @Test
    public void ensureWritingWorks() {
        configuration.addProperty("frapper", "dapper");
        configuration.save(pluginWrapper);
        assertThat(PluginConfiguration
                .read(pluginWrapper).getProperty("frapper"), is("dapper"));
    }

}