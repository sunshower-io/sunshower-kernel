package io.sunshower.kernel.api;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
public @interface ExtensionPoint {
    String namespace() default "__";
    String value() default "__default";
}