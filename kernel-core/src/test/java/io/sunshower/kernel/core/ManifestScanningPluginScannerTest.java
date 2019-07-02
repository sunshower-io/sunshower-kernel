package io.sunshower.kernel.core;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ManifestScanningPluginScannerTest {

  private PluginScanner scanner;
  private PluginScanner.PluginContext context;

  @BeforeEach
  void setUp() {
    scanner = new ManifestScanningPluginScanner();
    context = new PluginScanner.PluginContext(getClass(), ClassLoader.getSystemClassLoader());
  }

  @Test
  void ensureScanningClasspathWorks() {
    val result = scanner.scan(context);
    assertThat(result.isPresent(), is(true));
  }

  @Test
  void ensureGroupIsCorrect() {
    val result = scanner.scan(context).get();
    assertThat(result.getGroup(), is("io.sunshower.test"));
  }

  @Test
  void ensureNameIsCorrect() {
    val result = scanner.scan(context).get();
    assertThat(result.getName(), is("sunshower-test-core"));
  }

  @Test
  void ensureVersionIsCorrect() {
    val result = scanner.scan(context).get();
    assertThat(result.getVersion(), is("1.0-SNAPSHOT"));
  }

  @Test
  void ensureHashIsCorrect() {
    val result = scanner.scan(context).get();
    assertThat(result.getHash(), is("8341234141234"));
  }
}
