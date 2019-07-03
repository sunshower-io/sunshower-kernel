package io.sunshower.spring.processors;

import static org.junit.jupiter.api.Assertions.*;

class SpringPluginLifecycleTest {

  //  private SpringPluginLifecycle lifecycle;
  //
  //  @BeforeEach
  //  void setUp() {
  //    lifecycle = new SpringPluginLifecycle();
  //
  //    SpringPluginLifecycle.setPluginManager(mock(PluginManager.class));
  //    SpringPluginLifecycle.setEntryPoint(getClass());
  //    SpringPluginLifecycle.setPluginClassloader(ClassLoader.getSystemClassLoader());
  //    SpringPluginLifecycle.setCoordinate(new PluginCoordinate("1", "2", "3", "4"));
  //  }
  //
  //  @Test
  //  void ensurePluginLifecycleIsStoppedByDefault() {
  //    assertThat(lifecycle.isRunning(), is(false));
  //  }
  //
  //  @Test
  //  void ensurePluginThrowsExceptionIfClassloaderIsNull() {
  //    SpringPluginLifecycle.setPluginClassloader(null);
  //    assertThrows(PluginException.class, () -> lifecycle.start());
  //  }
  //
  //  @Test
  //  void ensurePluginThrowsExceptionIfPluginManagerIsNull() {
  //    SpringPluginLifecycle.setPluginManager(null);
  //    assertThrows(PluginException.class, () -> lifecycle.start());
  //  }
  //
  //  @Test
  //  void ensurePluginThrowsExceptionIfPluginEntryPointIsNull() {
  //    SpringPluginLifecycle.setEntryPoint(null);
  //    assertThrows(PluginException.class, () -> lifecycle.start());
  //  }
  //
  //  @Test
  //  void ensurePluginIsStartable() {
  //    lifecycle.start();
  //    assertTrue(lifecycle.isRunning());
  //  }
}
