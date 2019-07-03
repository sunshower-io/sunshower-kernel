package io.sunshower.spring;

import io.sunshower.api.PluginManager;
import io.sunshower.spring.processors.SpringPluginLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiTemplate;

@Configuration
public class SpringPluginConfiguration {

  @Bean
  public SpringPluginLifecycle springPluginLifecycle() {
    return new SpringPluginLifecycle();
  }

  @Bean
  public JndiTemplate jndiTemplate() {
    return new JndiTemplate();
  }

  @Bean
  public PluginManager kernelPluginManager(JndiTemplate template) {
    return new DelegatingPluginManager(template);
  }
}
