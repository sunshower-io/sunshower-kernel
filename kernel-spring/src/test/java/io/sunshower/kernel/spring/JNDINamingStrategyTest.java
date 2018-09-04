package io.sunshower.kernel.spring;

import io.sunshower.kernel.api.ExtensionCoordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JNDINamingStrategyTest {

  ExtensionCoordinate coordinate;
  JNDINamingStrategy namingStrategy;

  @BeforeEach
  void setUp() {
    coordinate =
        new ExtensionCoordinate(
            "hadsfadfaf.12341341", "dafadsfadf/\\coolbeans.whatever", "1.0.0-SNAPSHOT", "hello");
  }

  @Test
  void ensureNamingStrategyWorks() {
    assertThat(namingStrategy.normalize("h.b"), is("h_b"));
  }
}
