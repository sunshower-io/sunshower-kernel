package io.sunshower.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringPluginConfiguration {

  @Bean
  public String whatever() {
    return "HELLASFDHASDFAS";
  }
  //
  //  @Bean
  //  public SpringEntryPointAspect springEntryPointAspect() {
  //    return new SpringEntryPointAspect();
  //  }
}
