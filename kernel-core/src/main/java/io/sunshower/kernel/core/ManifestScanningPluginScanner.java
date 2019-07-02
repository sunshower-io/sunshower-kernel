package io.sunshower.kernel.core;

import io.sunshower.api.PluginCoordinate;
import io.sunshower.kernel.Chain;
import io.sunshower.kernel.LinkedChain;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.jar.Manifest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class ManifestScanningPluginScanner
    extends LinkedChain<PluginCoordinate, PluginScanner.PluginContext>
    implements PluginScanner, Chain<PluginCoordinate, PluginScanner.PluginContext> {

  @Override
  public Optional<PluginCoordinate> scan(PluginContext ctx) {
    return doProcess(ctx);
  }

  @Override
  protected Optional<PluginCoordinate> doProcess(PluginContext value) {
    log.info("Attempting to resolve plugin coordinate from manifest(s)");
    try {
      val resources = value.getClassLoader().getResources("META-INF/MANIFEST.MF");
      while (resources.hasMoreElements()) {
        val next = resources.nextElement();
        log.info("Attempting to read manifest from: {}", next);
        val manifest = doRead(next);
        if (manifest.isPresent()) {
          return manifest;
        }
      }
    } catch (IOException e) {
      log.warn("Failed to retrieve manifests...reason: {}", e);
    }
    return Optional.empty();
  }

  private Optional<PluginCoordinate> doRead(URL url) {
    try {
      val manifest = new Manifest(url.openStream());
      val attributes = manifest.getMainAttributes();
      if (attributes != null) {
        val name = attributes.getValue("name");
        val group = attributes.getValue("group");
        val version = attributes.getValue("version");
        val hash = attributes.getValue("hash");
        if (!(name == null || group == null || version == null)) {
          log.info(
              "Successfully located plugin[group:{}, name: {}, version: {}, hash: {}]",
              group,
              name,
              version,
              hash);
          return Optional.of(new PluginCoordinate(group, name, version, hash));
        }
      }
    } catch (IOException e) {
      log.info("Encountered issue attempting to parse manifest: {}.  Continuing", e.getMessage());
    }
    return Optional.empty();
  }
}
