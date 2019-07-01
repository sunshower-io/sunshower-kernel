package io.sunshower.wildfly.features;

import java.util.Base64;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.core.HttpHeaders;

final class BasicAuthenticator {

  private final HttpAuthenticationFilter.Credentials defaultCredentials;

  /**
   * Creates a new instance of basic authenticator.
   *
   * @param defaultCredentials Credentials. Can be {@code null} if no default credentials should be
   *     used.
   */
  BasicAuthenticator(HttpAuthenticationFilter.Credentials defaultCredentials) {
    this.defaultCredentials = defaultCredentials;
  }

  private String calculateAuthentication(HttpAuthenticationFilter.Credentials credentials) {
    String username = credentials.getUsername();
    byte[] password = credentials.getPassword();
    if (username == null) {
      username = "";
    }

    if (password == null) {
      password = new byte[0];
    }

    final byte[] prefix = (username + ":").getBytes(HttpAuthenticationFilter.CHARACTER_SET);
    final byte[] usernamePassword = new byte[prefix.length + password.length];

    System.arraycopy(prefix, 0, usernamePassword, 0, prefix.length);
    System.arraycopy(password, 0, usernamePassword, prefix.length, password.length);

    return "Basic " + Base64.getEncoder().encodeToString(usernamePassword);
  }

  /**
   * Adds authentication information to the request.
   *
   * @param request Request context.
   * @throws RequestAuthenticationException in case that basic credentials missing or are in invalid
   *     format
   */
  public void filterRequest(ClientRequestContext request) throws RequestAuthenticationException {
    HttpAuthenticationFilter.Credentials credentials =
        HttpAuthenticationFilter.getCredentials(
            request, defaultCredentials, HttpAuthenticationFilter.Type.BASIC);
    if (credentials == null) {
      throw new RequestAuthenticationException("Invalid credentials");
    }
    request.getHeaders().add(HttpHeaders.AUTHORIZATION, calculateAuthentication(credentials));
  }

  /**
   * Checks the response and if basic authentication is required then performs a new request with
   * basic authentication.
   *
   * @param request Request context.
   * @param response Response context (will be updated with newest response data if the request was
   *     repeated).
   * @return {@code true} if response does not require authentication or if authentication is
   *     required, new request was done with digest authentication information and authentication
   *     was successful.
   * @throws ResponseAuthenticationException in case that basic credentials missing or are in
   *     invalid format
   */
  public boolean filterResponseAndAuthenticate(
      ClientRequestContext request, ClientResponseContext response) {
    final String authenticate = response.getHeaders().getFirst(HttpHeaders.WWW_AUTHENTICATE);
    if (authenticate != null && authenticate.trim().toUpperCase().startsWith("BASIC")) {
      HttpAuthenticationFilter.Credentials credentials =
          HttpAuthenticationFilter.getCredentials(
              request, defaultCredentials, HttpAuthenticationFilter.Type.BASIC);

      if (credentials == null) {
        throw new ResponseAuthenticationException(null, "Missing basic authentication");
      }

      return HttpAuthenticationFilter.repeatRequest(
          request, response, calculateAuthentication(credentials));
    }
    return false;
  }
}
