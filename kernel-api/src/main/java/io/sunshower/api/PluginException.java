package io.sunshower.api;

import lombok.Getter;

@Getter
public class PluginException extends KernelException {
  private Plugin.Coordinate source;

  public PluginException(String message, Throwable cause, Plugin.Coordinate coordinate) {
    super(message, cause);
    this.source = coordinate;
  }

  public PluginException(String message) {
    super(message);
  }

  public PluginException(String message, Throwable cause) {
    super(message, cause);
  }
}
