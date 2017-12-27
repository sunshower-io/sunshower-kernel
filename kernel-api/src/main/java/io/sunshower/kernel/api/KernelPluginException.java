package io.sunshower.kernel.api;

public class KernelPluginException extends RuntimeException {

    public KernelPluginException() {
    }

    public KernelPluginException(String message) {
        super(message);
    }

    public KernelPluginException(String message, Throwable cause) {
        super(message, cause);
    }

    public KernelPluginException(Throwable cause) {
        super(cause);
    }

    public KernelPluginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
