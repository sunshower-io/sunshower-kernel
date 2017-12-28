package io.sunshower.kernel;

import io.sunshower.kernel.configuration.PluginConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.pf4j.DefaultPluginDescriptor;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.sunshower.kernel.PluginTestUtilities.pluginRoot;
import static io.sunshower.kernel.PluginTestUtilities.resolveFromRoot;
import static java.lang.ClassLoader.getSystemClassLoader;

public class KernelTestCase {


    protected PluginManager           pluginManager;
    protected PluginWrapper           pluginWrapper;
    protected DefaultPluginDescriptor pluginDescriptor;
    protected TestPlugin              plugin;
    protected PluginConfiguration     configuration;
    protected ConfigurationInjector   configurationInjector;
    private   File                    pluginDirectory;

    @BeforeEach
    public void setUp() {
        pluginDirectory = resolveFromRoot("test-plugin").toFile();
        pluginManager = new KernelPluginManager(
                pluginRoot(true)
        );
        Map cfg = new HashMap();
        cfg.put(YamlPluginDescriptor.GROUP_ID_NAME, "hello");
        cfg.put(YamlPluginDescriptor.PLUGIN_CLASS_NAME, "hello");
        pluginDescriptor = new YamlPluginDescriptor(
                cfg,
                pluginDirectory.toPath());

        pluginWrapper = new PluginWrapper(
                pluginManager,
                pluginDescriptor,
                pluginDirectory.toPath(),
                getSystemClassLoader()
        );
        plugin = new TestPlugin(pluginWrapper);
        configuration = new PluginConfiguration();
        configurationInjector =
                new ConfigurationInjector(TestPlugin.class, plugin);
    }
}
