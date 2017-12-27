package io.sunshower.kernel.test;

import org.pf4j.PluginRepository;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DirectoryPluginRepository implements PluginRepository {
    
    final File pluginRoot;

    public DirectoryPluginRepository(File pluginRoot) {
        Objects.requireNonNull(pluginRoot);
        this.pluginRoot = pluginRoot;
    }

    @Override
    public List<Path> getPluginPaths() {
        return Arrays.asList(pluginRoot.listFiles())
                .stream().map(File::toPath).collect(Collectors.toList());
    }

    @Override
    public boolean deletePluginPath(Path pluginPath) {
        return pluginPath.toFile().delete();
    }
}
