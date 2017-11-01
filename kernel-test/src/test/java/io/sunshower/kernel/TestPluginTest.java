package io.sunshower.kernel;

import io.sunshower.kernel.test.SunshowerPluginManager;
import org.junit.jupiter.api.Test;
import org.pf4j.PluginManager;

import java.util.List;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class TestPluginTest {
    
   
    @Test
    public void ensurePluginManagerWorks() {
        PluginManager pluginManager = new SunshowerPluginManager();
        pluginManager.loadPlugins();
        pluginManager.startPlugins();
        List<Plugin> extensions = pluginManager.getExtensions(Plugin.class);
        assertThat(extensions.size(), is(1));
    }
}
