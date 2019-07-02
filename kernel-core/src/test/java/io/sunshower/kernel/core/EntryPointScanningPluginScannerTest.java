package io.sunshower.kernel.core;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import io.sunshower.api.Coordinate;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntryPointScanningPluginScannerTest {

  @Coordinate(name = "w", version = "v", group = "g", hash = "h")
  static class CoordinateHolder {}

  private PluginScanner scanner;
  private PluginScanner.PluginContext context;

  @BeforeEach
  void setUp() {
    scanner = new EntryPointScanningPluginScanner();
    context =
        new PluginScanner.PluginContext(CoordinateHolder.class, ClassLoader.getSystemClassLoader());
  }

  @Test
  void ensureCoordinateIsCorrect() {
    val result = scanner.scan(context).get();
    assertThat(result.getGroup(), is("g"));
    assertThat(result.getVersion(), is("v"));
    assertThat(result.getHash(), is("h"));
    assertThat(result.getName(), is("w"));
  }
}
