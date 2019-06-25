package io.sunshower.kernel.wildfly.features;

import javax.ws.rs.ProcessingException;

/**
 * Exception thrown by security request authentication.
 *
 * @author Petr Bouda
 */
public class RequestAuthenticationException extends ProcessingException {

  /**
   * Creates new instance of this exception with exception cause.
   *
   * @param cause Exception cause.
   */
  public RequestAuthenticationException(Throwable cause) {
    super(cause);
  }

  /**
   * Creates new instance of this exception with exception message.
   *
   * @param message Exception message.
   */
  public RequestAuthenticationException(String message) {
    super(message);
  }

  /**
   * Creates new instance of this exception with exception message and exception cause.
   *
   * @param message Exception message.
   * @param cause Exception cause.
   */
  public RequestAuthenticationException(String message, Throwable cause) {
    super(message, cause);
  }
}
