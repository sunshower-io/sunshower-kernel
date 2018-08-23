package io.sunshower.kernel.api;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Fulfillment {
  String name() default "";
  Class<?> extensionPoint() default Object.class;
  Class<?> fulfillmentType() default Object.class;
}
