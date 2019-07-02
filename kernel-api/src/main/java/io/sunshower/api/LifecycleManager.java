package io.sunshower.api;

public interface LifecycleManager {

  void start();

  void stop();

  <T> T unwrap(Class<T> type);
}
