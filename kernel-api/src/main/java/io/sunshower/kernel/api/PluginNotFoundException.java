package io.sunshower.kernel.api;

public class PluginNotFoundException extends PluginException {

    public PluginNotFoundException(String message, Throwable cause, Plugin.Coordinate coordinate) {
        super(message, cause, coordinate);
    }
}
