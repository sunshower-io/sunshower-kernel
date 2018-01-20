package io.sunshower.kernel.test;

import io.sunshower.kernel.api.PluginManager;
import io.sunshower.kernel.api.PluginStorage;
import io.sunshower.kernel.testplugins.ThemeManager;
import io.sunshower.test.common.TestClasspath;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class KernelSystemTest {

    static File file(String path) {
        return new File(TestClasspath.buildDir().getParentFile(), path);
    }
    
    @Resource(
            name = "java:global/kernel-wildfly-provider-1.0.0-SNAPSHOT/InMemoryPluginStorage!io.sunshower.kernel.api.PluginStorage"
    )
    private PluginStorage pluginStorage;

    @Resource(
            name = "java:global/kernel-wildfly-provider-1.0.0-SNAPSHOT/WildflyPluginManager!io.sunshower.kernel.api.PluginManager"
    )
    private PluginManager pluginManager;

    @Resource(name = "java:global/simple-test-1.0.0-SNAPSHOT/DefaultThemeManager!io.sunshower.kernel.testplugins.ThemeManager")
    private ThemeManager themeManager;
    
    @Deployment
    public static WebArchive webArchive() {
        return ShrinkWrap.create(WebArchive.class, "kernel-test-war.war")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource(file("src/test/webapp/WEB-INF/jboss-deployment-structure.xml"))
                .addClass(KernelSystemTest.class)
                .addClass(TestClasspath.class);
    }


    @Test
    public void ensurePluginClassIsLoadable() throws ClassNotFoundException {
        Class.forName("io.sunshower.kernel.api.ExtensionPoint");
    }
    
    @Test
    public void ensurePluginStorageIsInjectable() {
        assertNotNull(pluginStorage);
    }
    
    @Test
    public void ensureWildflyPluginStorageIsAvailableAtJNDILocation() {
        assertNotNull(themeManager.getActiveTheme());
    }
    
    public void setPluginManager(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }
    
    @Test
    public void ensureThemeManagerHasCorrectNumberOfThemes() {
        assertEquals(pluginManager.resolve(ThemeManager.class).themes().size(), 2);
    }


}
