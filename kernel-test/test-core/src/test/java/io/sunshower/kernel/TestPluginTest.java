package io.sunshower.kernel;

import io.sunshower.kernel.test.TestPluginManager;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.pf4j.PluginManager;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


@RunWith(JUnitPlatform.class)
public class TestPluginTest {
    
    static final Logger logger = Logger.getLogger(TestPluginTest.class.getName());

    @Test
    public void ensurePluginManagerWorks() {
        PluginManager pluginManager = new TestPluginManager(pluginRoot());
        pluginManager.loadPlugins();
        pluginManager.startPlugins();
        List<Plugin> extensions = pluginManager.getExtensions(Plugin.class);
        assertThat(extensions.size(), is(1));
    }
    
    final Path pluginRoot() {
        final File file = cwd();
        for(File p = file; p != null; p = p.getParentFile()) {
            final File candidate = new File(p, "build");
            if(candidate.exists()) {
                File pluginDirectory = new File(candidate, "plugins");
                assertTrue(pluginDirectory.exists());
                logger.log(Level.INFO, "Using file: {}", pluginDirectory);
                return pluginDirectory.toPath();
            }
        }
        throw new IllegalStateException("Plugin root not found!");
    }

    private File cwd() {
        return new File(ClassLoader.getSystemResource(".").getFile());
    }
}
