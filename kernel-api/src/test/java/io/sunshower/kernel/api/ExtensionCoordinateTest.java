package io.sunshower.kernel.api;

import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
class ExtensionCoordinateTest {

  @Test
  public void ensureCoordinateBuilderMakesSense() {
    ExtensionCoordinate build =
        ExtensionCoordinate.builder()
            .group("test")
            .version("1.0")
            .namespace("default")
            .name("beans")
            .build();
  }
}
