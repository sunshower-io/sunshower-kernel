package io.sunshower.kernel.core;

import io.sunshower.api.Coordinate;
import io.sunshower.api.PluginCoordinate;
import io.sunshower.api.PluginException;
import io.sunshower.kernel.Chain;
import io.sunshower.kernel.LinkedChain;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class EntryPointScanningPluginScanner
    extends LinkedChain<PluginCoordinate, PluginScanner.PluginContext>
    implements PluginScanner, Chain<PluginCoordinate, PluginScanner.PluginContext> {

  @Override
  protected Optional<PluginCoordinate> doProcess(PluginContext value) {
    val entryPoint = value.getEntryPoint();
    log.info("Attempting to retrieve plugin coordinate from entry point class: {}", entryPoint);
    if (entryPoint == null) {
      throw new PluginException(
          "Cannot attempt to scan a null entry-point--this is probably a configuration error");
    }
    val coordinate = entryPoint.getAnnotation(Coordinate.class);
    if (coordinate == null) {
      log.info("No @Coordinate annotation found...delegating to next");
      return Optional.empty();
    }
    return Optional.of(
        new PluginCoordinate(
            coordinate.group(), coordinate.name(), coordinate.version(), coordinate.hash()));
  }

  @Override
  public Optional<PluginCoordinate> scan(PluginContext ctx) {
    return doProcess(ctx);
  }
}
