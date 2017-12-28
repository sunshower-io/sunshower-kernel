package io.sunshower.kernel;

import org.pf4j.PluginRepository;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirectoryPluginRepository implements PluginRepository {
    
    static final Logger logger = Logger.getLogger(DirectoryPluginRepository.class.getName());
    
    final File pluginRoot;

    public DirectoryPluginRepository(File pluginRoot) {
        Objects.requireNonNull(pluginRoot);
        this.pluginRoot = pluginRoot;
    }

    @Override
    public List<Path> getPluginPaths() {
        File[] files = pluginRoot.listFiles(File::isDirectory);
        if(files != null) {
            List<Path> paths = Arrays.asList(files)
                    .stream()
                    .flatMap(this::moduleFile)
                    .map(File::toPath)
                    .collect(Collectors.toList());
            
            logPathsLoaded(paths);
            return paths;

        }
        return Collections.emptyList();
    }

    private void logPathsLoaded(List<Path> paths) {
        if(logger.isLoggable(Level.FINE)) {
            for(Path path : paths) {
                logger.log(Level.FINE, "Loaded plugin: {0}", path);
            }
        }
    }

    private Stream<? extends File> moduleFile(File t) {
        File[] fs = t.listFiles(u -> 
                String.format("%s.jar", t.getName()
                ).equals(u.getName()));
        if(fs != null) {
            return Stream.of(fs);
        } else {
            return Stream.empty();
        }
    }

    @Override
    public boolean deletePluginPath(Path pluginPath) {
        return pluginPath.toFile().delete();
    }
    
    
}
