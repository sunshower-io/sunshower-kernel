package io.sunshower.kernel.configuration;

import io.sunshower.kernel.KernelTestCase;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


@RunWith(JUnitPlatform.class)
public class PluginConfigurationTest extends KernelTestCase {
   
    @Test
    public void ensureWritingWorks() {
        configuration.addProperty("frapper", "dapper");
        configuration.save(pluginWrapper);
        assertThat(PluginConfiguration
                .read(pluginWrapper).getProperty("frapper"), is("dapper"));
    }

}