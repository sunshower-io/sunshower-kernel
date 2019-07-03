package io.sunshower.wildfly;

import io.sunshower.wildfly.extensions.AuthenticationExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SunshowerKernelConfiguration {

  @Bean
  public AuthenticationExtension extension() {
    return new AuthenticationExtension() {};
  }
}
