package io.sunshower.spring;

import io.sunshower.spring.processors.SpringPluginLifecycle;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class LifecycleAwareSpringAnnotationConfigWebApplicationContext
    extends AnnotationConfigWebApplicationContext {

  private final SpringPluginLifecycle lifecycle;

  public LifecycleAwareSpringAnnotationConfigWebApplicationContext(
      SpringPluginLifecycle lifecycle) {
    super();
    this.lifecycle = lifecycle;
  }

  protected void prepareBeanFactory(ConfigurableListableBeanFactory factory) {
    super.prepareBeanFactory(factory);
    factory.registerSingleton("spring::lifecycle", lifecycle);
  }
}
