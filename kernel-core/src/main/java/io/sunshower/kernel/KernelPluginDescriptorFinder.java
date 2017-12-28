package io.sunshower.kernel;

import org.pf4j.PluginDescriptor;
import org.pf4j.PluginDescriptorFinder;
import org.pf4j.PluginException;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class KernelPluginDescriptorFinder implements PluginDescriptorFinder {
    
    
    @Override
    public boolean isApplicable(Path pluginPath) {
        final String pathName = pluginPath.toString();
        if(pathName.endsWith(".jar") || pathName.endsWith(".war") || pathName.endsWith(".zip")) {
            try {
                final JarFile jarFile = new JarFile(pluginPath.toFile());
                final Manifest manifest = jarFile.getManifest();
                if(manifest != null) {
                    final Attributes attributes = manifest.getMainAttributes();
                    
                    if (attributes != null) {
                        if("true".equals(attributes.getValue("sunshower-plugin"))) {
                            return true;
                        }
                    }
                }
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public PluginDescriptor find(Path pluginPath) throws PluginException {
        try {
            final ZipFile zipFile = new ZipFile(pluginPath.toFile());
            ZipEntry entry = zipFile.getEntry("plugin.yml");
            if(entry != null) {
                return readEntry(zipFile, entry, pluginPath);
            }
           
            entry = zipFile.getEntry("BOOT-INF/classes/plugin.yml");
            if(entry != null) {
                return readEntry(zipFile, entry, pluginPath);
            }
            
            throw new InvalidPluginException(String.format("attempted to " +
                    "load plugin from '%s' but failed.  " +
                    "Reason: No plugin.yml found", pluginPath));
        } catch (IOException e) {
            throw new PluginException(e);
        }
    }

    private PluginDescriptor readEntry(ZipFile zipFile, ZipEntry entry, Path pluginPath) throws IOException {
        final Yaml yaml = new Yaml();
        Map load = yaml.load(zipFile.getInputStream(entry));
        return new YamlPluginDescriptor(load, pluginPath);
    }
}
