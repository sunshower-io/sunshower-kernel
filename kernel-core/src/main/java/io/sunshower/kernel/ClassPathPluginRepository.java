package io.sunshower.kernel;

import org.pf4j.PluginRepository;

import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ClassPathPluginRepository implements PluginRepository {
    
    
    @Override
    public List<Path> getPluginPaths() {
        ClassLoader classloader = ClassLoader.getSystemClassLoader();
        URL[] urls = ((URLClassLoader) classloader).getURLs();
        return Arrays.stream(urls).map(t -> {
            try {
                return Paths.get(t.toURI());
            } catch (URISyntaxException e) {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public boolean deletePluginPath(Path pluginPath) {
        return false;
    }
}
