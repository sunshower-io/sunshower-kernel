package io.sunshower.api;

public class PluginNotFoundException extends PluginException {

  public PluginNotFoundException(String message, Throwable cause, PluginCoordinate coordinate) {
    super(message, cause, coordinate);
  }
}
