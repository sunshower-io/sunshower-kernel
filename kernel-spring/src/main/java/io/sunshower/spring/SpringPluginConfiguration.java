package io.sunshower.spring;

import io.sunshower.api.PluginManager;
import io.sunshower.spring.processors.SpringPluginLifecycle;
import javax.naming.NamingException;
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
  public PluginManager kernelPluginManager(JndiTemplate template) throws NamingException {
    return template.lookup("java:global/sunshower/kernel/plugin-manager", PluginManager.class);
  }
}
