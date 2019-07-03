package io.sunshower.api;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PluginConfiguration {
  String path() default "$$default$$";

  Plugin.Type type() default Plugin.Type.Extension;

  Class<?> configurationClass() default void.class;
}
