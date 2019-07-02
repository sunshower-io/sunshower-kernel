package io.sunshower.kernel.core;

import io.sunshower.api.PluginCoordinate;
import java.util.Optional;
import lombok.Data;
import lombok.val;

public interface PluginScanner {

  @Data
  class PluginContext {
    private final Class<?> entryPoint;
    private final ClassLoader classLoader;
  }

  Optional<PluginCoordinate> scan(PluginContext ctx);

  static PluginScanner getDefaultChain() {
    val result = new ManifestScanningPluginScanner();
    result.setNext(new EntryPointScanningPluginScanner());
    return result;
  }
}
