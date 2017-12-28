package io.sunshower.kernel;

import org.pf4j.DefaultPluginLoader;
import org.pf4j.PluginClassLoader;
import org.pf4j.PluginClasspath;
import org.pf4j.PluginManager;
import org.pf4j.util.FileUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class KernelPluginLoader extends DefaultPluginLoader {
    public KernelPluginLoader(PluginManager pluginManager, PluginClasspath pluginClasspath) {
        super(pluginManager, pluginClasspath);
    }

    @Override
    public boolean isApplicable(Path pluginPath) {
        return hasLib(pluginPath) || super.isApplicable(pluginPath);
    }

    private boolean hasLib(Path pluginPath) {
        File file = pluginPath.toFile();
        return file.exists() && new File(file.getParentFile(), "lib").exists();
    }


    protected void loadJars(Path pluginPath, PluginClassLoader pluginClassLoader) {
        pluginClassLoader.addFile(pluginPath.toFile());
        for (String libDirectory : pluginClasspath.getLibDirectories()) {
            Path       file = pluginPath.getParent().resolve(libDirectory);
            List<File> jars = FileUtils.getJars(file);
            for (File jar : jars) {
                pluginClassLoader.addFile(jar);
            }
        }
    }
    
    
}
