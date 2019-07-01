package io.sunshower.wildfly.features;

import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.core.Response;

public class ResponseAuthenticationException extends ResponseProcessingException {

  /**
   * Creates new instance of this exception with exception cause.
   *
   * @param response the response instance for which the processing failed.
   * @param cause Exception cause.
   */
  public ResponseAuthenticationException(Response response, Throwable cause) {
    super(response, cause);
  }

  /**
   * Creates new instance of this exception with exception message.
   *
   * @param response the response instance for which the processing failed.
   * @param message Exception message.
   */
  public ResponseAuthenticationException(Response response, String message) {
    super(response, message);
  }

  /**
   * Creates new instance of this exception with exception message and exception cause.
   *
   * @param response the response instance for which the processing failed.
   * @param message Exception message.
   * @param cause Exception cause.
   */
  public ResponseAuthenticationException(Response response, String message, Throwable cause) {
    super(response, message, cause);
  }
}
