package io.sunshower.api;

public interface ResourceLoader {
  Resource getResource();

  ClassLoader getClassloader();
}
