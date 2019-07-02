package io.sunshower.kernel.core;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PluginScannerTest {

  private PluginScanner.PluginContext context;

  @BeforeEach
  void setUp() {
    context = new PluginScanner.PluginContext(getClass(), ClassLoader.getSystemClassLoader());
  }

  @Test
  void ensureChainIsCorrect() {
    val result = PluginScanner.getDefaultChain().scan(context);
    assertTrue(result.isPresent());
    val coord = result.get();
    assertThat(coord.getGroup(), is("io.sunshower.test"));
  }
}
