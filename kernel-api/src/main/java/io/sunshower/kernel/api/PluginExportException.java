package io.sunshower.kernel.api;

public class PluginExportException extends KernelException {
  public PluginExportException(String message, Throwable cause) {
    super(message, cause);
  }

  public PluginExportException(Throwable cause) {
    super(cause);
  }
}
