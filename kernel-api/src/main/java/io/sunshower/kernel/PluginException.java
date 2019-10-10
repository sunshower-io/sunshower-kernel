package io.sunshower.kernel;

public class PluginException extends KernelException {

  public PluginException() {}

  public PluginException(String message) {
    super(message);
  }

  public PluginException(String message, Throwable cause) {
    super(message, cause);
  }

  public PluginException(Throwable cause) {
    super(cause);
  }

  public PluginException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
