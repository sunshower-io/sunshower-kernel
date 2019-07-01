package io.sunshower.wildfly.features;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

public class HttpAuthenticationFeature implements Feature {

  /** Feature authentication mode. */
  static enum Mode {
    /** Basic preemptive. */
    BASIC_PREEMPTIVE,
    /** Basic non preemptive */
    BASIC_NON_PREEMPTIVE,
    /** Digest. */
    DIGEST,
    /** Universal. */
    UNIVERSAL
  }

  /** Builder that creates instances of {@link HttpAuthenticationFeature}. */
  public static interface Builder {

    /**
     * Set credentials.
     *
     * @param username Username.
     * @param password Password as byte array.
     * @return This builder.
     */
    public Builder credentials(String username, byte[] password);

    /**
     * Set credentials.
     *
     * @param username Username.
     * @param password Password as {@link String}.
     * @return This builder.
     */
    public Builder credentials(String username, String password);

    /**
     * Build the feature.
     *
     * @return Http authentication feature configured from this builder.
     */
    public HttpAuthenticationFeature build();
  }

  /** that builds the http authentication feature configured for basic authentication. */
  public static interface BasicBuilder extends Builder {

    /**
     * Configure the builder to create features in non-preemptive basic authentication mode.
     *
     * @return This builder.
     */
    public BasicBuilder nonPreemptive();
  }

  /**
   * that builds the http authentication feature configured in universal mode that supports basic
   * and digest authentication.
   */
  public static interface UniversalBuilder extends Builder {

    /**
     * Set credentials that will be used for basic authentication only.
     *
     * @param username Username.
     * @param password Password as {@link String}.
     * @return This builder.
     */
    public UniversalBuilder credentialsForBasic(String username, String password);

    /**
     * Set credentials that will be used for basic authentication only.
     *
     * @param username Username.
     * @param password Password as {@code byte array}.
     * @return This builder.
     */
    public UniversalBuilder credentialsForBasic(String username, byte[] password);

    /**
     * Set credentials that will be used for digest authentication only.
     *
     * @param username Username.
     * @param password Password as {@link String}.
     * @return This builder.
     */
    public UniversalBuilder credentialsForDigest(String username, String password);

    /**
     * Set credentials that will be used for digest authentication only.
     *
     * @param username Username.
     * @param password Password as {@code byte array}.
     * @return This builder.
     */
    public UniversalBuilder credentialsForDigest(String username, byte[] password);
  }

  /** Implementation of all authentication builders. */
  static class BuilderImpl implements UniversalBuilder, BasicBuilder {

    private String usernameBasic;
    private byte[] passwordBasic;
    private String usernameDigest;
    private byte[] passwordDigest;
    private Mode mode;

    /**
     * Create a new builder.
     *
     * @param mode Mode in which the final authentication feature should work.
     */
    public BuilderImpl(Mode mode) {
      this.mode = mode;
    }

    @Override
    public Builder credentials(String username, String password) {
      return credentials(
          username,
          password == null ? null : password.getBytes(HttpAuthenticationFilter.CHARACTER_SET));
    }

    @Override
    public Builder credentials(String username, byte[] password) {
      credentialsForBasic(username, password);
      credentialsForDigest(username, password);
      return this;
    }

    @Override
    public UniversalBuilder credentialsForBasic(String username, String password) {
      return credentialsForBasic(
          username,
          password == null ? null : password.getBytes(HttpAuthenticationFilter.CHARACTER_SET));
    }

    @Override
    public UniversalBuilder credentialsForBasic(String username, byte[] password) {
      this.usernameBasic = username;
      this.passwordBasic = password;
      return this;
    }

    @Override
    public UniversalBuilder credentialsForDigest(String username, String password) {
      return credentialsForDigest(
          username,
          password == null ? null : password.getBytes(HttpAuthenticationFilter.CHARACTER_SET));
    }

    @Override
    public UniversalBuilder credentialsForDigest(String username, byte[] password) {
      this.usernameDigest = username;
      this.passwordDigest = password;
      return this;
    }

    @Override
    public HttpAuthenticationFeature build() {
      return new HttpAuthenticationFeature(
          mode,
          usernameBasic == null
              ? null
              : new HttpAuthenticationFilter.Credentials(usernameBasic, passwordBasic),
          usernameDigest == null
              ? null
              : new HttpAuthenticationFilter.Credentials(usernameDigest, passwordDigest));
    }

    @Override
    public BasicBuilder nonPreemptive() {
      if (mode == Mode.BASIC_PREEMPTIVE) {
        this.mode = Mode.BASIC_NON_PREEMPTIVE;
      }
      return this;
    }
  }

  /**
   * Key of the property that can be set into the {@link javax.ws.rs.client.ClientRequestContext
   * client request} using {@link javax.ws.rs.client.ClientRequestContext#setProperty(String,
   * Object)} in order to override the username for http authentication feature for the request.
   *
   * <p>Example:
   *
   * <pre>
   * Response response = client.target("http://localhost:8080/rest/joe/orders").request()
   *      .property(HTTP_AUTHENTICATION_USERNAME, "joe")
   *      .property(HTTP_AUTHENTICATION_PASSWORD, "p1swd745").get();
   * </pre>
   *
   * The property must be always combined with configuration of {@link
   * #HTTP_AUTHENTICATION_PASSWORD} property (as shown in the example). This property pair overrides
   * all password settings of the authentication feature for the current request.
   *
   * <p>The default value must be instance of {@link String}.
   *
   * <p>The name of the configuration property is <tt>{@value}</tt>.
   */
  public static final String HTTP_AUTHENTICATION_USERNAME =
      "jersey.config.client.http.auth.username";
  /**
   * Key of the property that can be set into the {@link javax.ws.rs.client.ClientRequestContext
   * client request} using {@link javax.ws.rs.client.ClientRequestContext#setProperty(String,
   * Object)} in order to override the password for http authentication feature for the request.
   *
   * <p>Example:
   *
   * <pre>
   * Response response = client.target("http://localhost:8080/rest/joe/orders").request()
   *      .property(HTTP_AUTHENTICATION_USERNAME, "joe")
   *      .property(HTTP_AUTHENTICATION_PASSWORD, "p1swd745").get();
   * </pre>
   *
   * The property must be always combined with configuration of {@link
   * #HTTP_AUTHENTICATION_USERNAME} property (as shown in the example). This property pair overrides
   * all password settings of the authentication feature for the current request.
   *
   * <p>The value must be instance of {@link String} or {@code byte} array ({@code byte[]}).
   *
   * <p>The name of the configuration property is <tt>{@value}</tt>.
   */
  public static final String HTTP_AUTHENTICATION_PASSWORD =
      "jersey.config.client.http.auth.password";

  /**
   * Key of the property that can be set into the {@link javax.ws.rs.client.ClientRequestContext
   * client request} using {@link javax.ws.rs.client.ClientRequestContext#setProperty(String,
   * Object)} in order to override the username for http basic authentication feature for the
   * request.
   *
   * <p>Example:
   *
   * <pre>
   * Response response = client.target("http://localhost:8080/rest/joe/orders").request()
   *      .property(HTTP_AUTHENTICATION_BASIC_USERNAME, "joe")
   *      .property(HTTP_AUTHENTICATION_BASIC_PASSWORD, "p1swd745").get();
   * </pre>
   *
   * The property must be always combined with configuration of {@link
   * #HTTP_AUTHENTICATION_PASSWORD} property (as shown in the example). The property pair influence
   * only credentials used during basic authentication.
   *
   * <p>The value must be instance of {@link String}.
   *
   * <p>The name of the configuration property is <tt>{@value}</tt>.
   */
  public static final String HTTP_AUTHENTICATION_BASIC_USERNAME =
      "jersey.config.client.http.auth.basic.username";

  /**
   * Key of the property that can be set into the {@link javax.ws.rs.client.ClientRequestContext
   * client request} using {@link javax.ws.rs.client.ClientRequestContext#setProperty(String,
   * Object)} in order to override the password for http basic authentication feature for the
   * request.
   *
   * <p>Example:
   *
   * <pre>
   * Response response = client.target("http://localhost:8080/rest/joe/orders").request()
   *      .property(HTTP_AUTHENTICATION_BASIC_USERNAME, "joe")
   *      .property(HTTP_AUTHENTICATION_BASIC_PASSWORD, "p1swd745").get();
   * </pre>
   *
   * The property must be always combined with configuration of {@link
   * #HTTP_AUTHENTICATION_USERNAME} property (as shown in the example). The property pair influence
   * only credentials used during basic authentication.
   *
   * <p>The value must be instance of {@link String} or {@code byte} array ({@code byte[]}).
   *
   * <p>The name of the configuration property is <tt>{@value}</tt>.
   */
  public static final String HTTP_AUTHENTICATION_BASIC_PASSWORD =
      "jersey.config.client.http.auth.basic.password";

  /**
   * Key of the property that can be set into the {@link javax.ws.rs.client.ClientRequestContext
   * client request} using {@link javax.ws.rs.client.ClientRequestContext#setProperty(String,
   * Object)} in order to override the username for http digest authentication feature for the
   * request.
   *
   * <p>Example:
   *
   * <pre>
   * Response response = client.target("http://localhost:8080/rest/joe/orders").request()
   *      .property(HTTP_AUTHENTICATION_DIGEST_USERNAME, "joe")
   *      .property(HTTP_AUTHENTICATION_DIGEST_PASSWORD, "p1swd745").get();
   * </pre>
   *
   * The property must be always combined with configuration of {@link
   * #HTTP_AUTHENTICATION_PASSWORD} property (as shown in the example). The property pair influence
   * only credentials used during digest authentication.
   *
   * <p>The value must be instance of {@link String}.
   *
   * <p>The name of the configuration property is <tt>{@value}</tt>.
   */
  public static final String HTTP_AUTHENTICATION_DIGEST_USERNAME =
      "jersey.config.client.http.auth.digest.username";

  /**
   * Key of the property that can be set into the {@link javax.ws.rs.client.ClientRequestContext
   * client request} using {@link javax.ws.rs.client.ClientRequestContext#setProperty(String,
   * Object)} in order to override the password for http digest authentication feature for the
   * request.
   *
   * <p>Example:
   *
   * <pre>
   * Response response = client.target("http://localhost:8080/rest/joe/orders").request()
   *      .property(HTTP_AUTHENTICATION_DIGEST_USERNAME, "joe")
   *      .property(HTTP_AUTHENTICATION_DIGEST_PASSWORD, "p1swd745").get();
   * </pre>
   *
   * The property must be always combined with configuration of {@link
   * #HTTP_AUTHENTICATION_PASSWORD} property (as shown in the example). The property pair influence
   * only credentials used during digest authentication.
   *
   * <p>The value must be instance of {@link String} or {@code byte} array ({@code byte[]}).
   *
   * <p>The name of the configuration property is <tt>{@value}</tt>.
   */
  public static final String HTTP_AUTHENTICATION_DIGEST_PASSWORD =
      "jersey.config.client.http.auth.digest.password";

  /**
   * Create the builder of the http authentication feature working in basic authentication mode. The
   * builder can build preemptive and non-preemptive basic authentication features.
   *
   * @return Basic http authentication builder.
   */
  public static BasicBuilder basicBuilder() {
    return new BuilderImpl(Mode.BASIC_PREEMPTIVE);
  }

  /**
   * Create the http authentication feature in basic preemptive authentication mode initialized with
   * credentials.
   *
   * @param username Username.
   * @param password Password as {@code byte array}.
   * @return Http authentication feature configured in basic mode.
   */
  public static HttpAuthenticationFeature basic(String username, byte[] password) {
    return build(Mode.BASIC_PREEMPTIVE, username, password);
  }

  /**
   * Create the http authentication feature in basic preemptive authentication mode initialized with
   * credentials.
   *
   * @param username Username.
   * @param password Password as {@link String}.
   * @return Http authentication feature configured in basic mode.
   */
  public static HttpAuthenticationFeature basic(String username, String password) {
    return build(Mode.BASIC_PREEMPTIVE, username, password);
  }

  /**
   * Create the http authentication feature in digest authentication mode initialized without
   * default credentials. Credentials will have to be supplied using request properties for each
   * request.
   *
   * @return Http authentication feature configured in digest mode.
   */
  public static HttpAuthenticationFeature digest() {
    return build(Mode.DIGEST);
  }

  /**
   * Create the http authentication feature in digest authentication mode initialized with
   * credentials.
   *
   * @param username Username.
   * @param password Password as {@code byte array}.
   * @return Http authentication feature configured in digest mode.
   */
  public static HttpAuthenticationFeature digest(String username, byte[] password) {
    return build(Mode.DIGEST, username, password);
  }

  /**
   * Create the http authentication feature in digest authentication mode initialized with
   * credentials.
   *
   * @param username Username.
   * @param password Password as {@link String}.
   * @return Http authentication feature configured in digest mode.
   */
  public static HttpAuthenticationFeature digest(String username, String password) {
    return build(Mode.DIGEST, username, password);
  }

  /**
   * Create the builder that builds http authentication feature in combined mode supporting both,
   * basic and digest authentication.
   *
   * @return Universal builder.
   */
  public static UniversalBuilder universalBuilder() {
    return new BuilderImpl(Mode.UNIVERSAL);
  }

  /**
   * Create the http authentication feature in combined mode supporting both, basic and digest
   * authentication.
   *
   * @param username Username.
   * @param password Password as {@code byte array}.
   * @return Http authentication feature configured in digest mode.
   */
  public static HttpAuthenticationFeature universal(String username, byte[] password) {
    return build(Mode.UNIVERSAL, username, password);
  }

  /**
   * Create the http authentication feature in combined mode supporting both, basic and digest
   * authentication.
   *
   * @param username Username.
   * @param password Password as {@link String}.
   * @return Http authentication feature configured in digest mode.
   */
  public static HttpAuthenticationFeature universal(String username, String password) {
    return build(Mode.UNIVERSAL, username, password);
  }

  private static HttpAuthenticationFeature build(Mode mode) {
    return new BuilderImpl(mode).build();
  }

  private static HttpAuthenticationFeature build(Mode mode, String username, byte[] password) {
    return new BuilderImpl(mode).credentials(username, password).build();
  }

  private static HttpAuthenticationFeature build(Mode mode, String username, String password) {
    return new BuilderImpl(mode).credentials(username, password).build();
  }

  private final Mode mode;
  private final HttpAuthenticationFilter.Credentials basicCredentials;
  private final HttpAuthenticationFilter.Credentials digestCredentials;

  private HttpAuthenticationFeature(
      Mode mode,
      HttpAuthenticationFilter.Credentials basicCredentials,
      HttpAuthenticationFilter.Credentials digestCredentials) {
    this.mode = mode;
    this.basicCredentials = basicCredentials;

    this.digestCredentials = digestCredentials;
  }

  @Override
  public boolean configure(FeatureContext context) {
    context.register(
        new HttpAuthenticationFilter(
            mode, basicCredentials, digestCredentials, context.getConfiguration()));
    return true;
  }
}
