package io.sunshower.api;

@ExtensionPoint
public interface ConfigurationManager {

  Object getConfiguration(Class<?> cfg);
}
