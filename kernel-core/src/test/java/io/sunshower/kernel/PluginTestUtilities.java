package io.sunshower.kernel;

import java.io.File;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import static junit.framework.TestCase.assertTrue;

public class PluginTestUtilities {
    static final Logger logger = Logger.getLogger(Plugin.class.getName());
   
    public static Path resolveFromRoot(String relative) {
        return pluginRoot().resolve(relative);
    }
    
    public static final Path pluginRoot() {
        return pluginRoot(false);
    }
    
    public static final Path pluginRoot(boolean create) {
        final File file = cwd();
        for(File p = file; p != null; p = p.getParentFile()) {
            final File candidate = new File(p, "build");
            if(candidate.exists()) {
                File pluginDirectory = new File(candidate, "plugins");
                logger.log(Level.INFO, "Using directory: {0} as plugin root", pluginDirectory);
                return pluginDirectory.toPath();
            } else {
                if(create) {
                    File pluginDirectory = new File(candidate, "plugins");
                    assertTrue(pluginDirectory.mkdirs());
                    logger.log(Level.INFO, "Creating directory: {0} as plugin root", pluginDirectory);
                    return pluginDirectory.toPath();
                }
            }
        }
        throw new IllegalStateException("Plugin root not found!");
    }

    public static File cwd() {
        return new File(ClassLoader.getSystemResource(".").getFile());
    }
}
