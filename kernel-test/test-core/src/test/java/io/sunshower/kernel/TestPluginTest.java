package io.sunshower.kernel;

import io.sunshower.kernel.test.LifecycleExposedPlugin;
import io.sunshower.kernel.test.PluginStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.sunshower.kernel.PluginTestUtilities.pluginRoot;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


@RunWith(JUnitPlatform.class)
public class TestPluginTest {
    
    static final Logger logger = Logger.getLogger(TestPluginTest.class.getName());
    private KernelPluginManager pluginManager;

    @BeforeEach
    public void setUp() {
        pluginManager = new KernelPluginManager(pluginRoot());
    }
    
    @AfterEach
    public void tearDown() {
        pluginManager.stopPlugins();
    }

    @Test
    public void ensurePluginManagerWorks() {
        pluginManager.loadPlugins();
        pluginManager.startPlugins();
        List<PluginWrapper> extensions = pluginManager.getStartedPlugins();
        assertThat(extensions.size(), is(2));
    }
   
    @Test
    public void ensureLoadIsCalledOnTestPlugin() {
        pluginManager.loadPlugins();
        pluginManager.startPlugins();
        Plugin plugin = pluginManager.getStartedPlugins().get(0).getPlugin();
       
        LifecycleExposedPlugin lfPlugin = (LifecycleExposedPlugin) plugin;
        assertEquals(lfPlugin.getState(), PluginStatus.Started);
    }


    @Test
    public void ensureLoadIsCalledOnStop() {
        pluginManager.loadPlugins();
        pluginManager.startPlugins();
        pluginManager.stopPlugins();
        Plugin plugin = pluginManager.getPlugins().get(1).getPlugin();
        LifecycleExposedPlugin lfPlugin = (LifecycleExposedPlugin) plugin;
        assertEquals(lfPlugin.getState(), PluginStatus.Stopped);
    }
    
}
