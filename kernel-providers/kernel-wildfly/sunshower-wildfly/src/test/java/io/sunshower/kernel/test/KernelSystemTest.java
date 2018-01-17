package io.sunshower.kernel.test;

import io.sunshower.kernel.api.PluginManager;
import io.sunshower.kernel.api.PluginStorage;
import io.sunshower.kernel.spi.EphemeralPluginStorage;
import io.sunshower.kernel.wildfly.WildflyPluginManager;
import io.sunshower.test.common.TestClasspath;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class KernelSystemTest {

    static File file(String path) {
        return new File(TestClasspath.buildDir().getParentFile(), path);
    }
    
    @Inject
    private PluginStorage pluginStorage;
    
    @Inject
    private PluginManager pluginManager;
    
    @Deployment
    public static WebArchive webArchive() {
        return ShrinkWrap.create(WebArchive.class)
                .addAsWebInfResource(file("src/test/webapp/WEB-INF/jboss-deployment-structure.xml"))
                .addClass(KernelSystemTest.class)
                .addClass(TestClasspath.class)
                .addClass(WildflyPluginManager.class)
                .addClass(EphemeralPluginStorage.class)
//                .addClass(ThemeManager.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Test
    public void ensurePluginClassIsLoadable() throws ClassNotFoundException {
        Class.forName("io.sunshower.kernel.api.ExtensionPoint");
    }
    
    @Test
    public void ensurePluginStorageIsInjectable() {
        assertNotNull(pluginStorage);
    }
    
//    @Test
//    public void ensureWildflyPluginStorageIsAvailableAtJNDILocation() {
//        assertEquals(pluginManager.resolve(ThemeManager.class).size(), 1);
//    }
    
    
}
