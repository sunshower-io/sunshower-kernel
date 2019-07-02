package io.sunshower;

import io.sunshower.spring.SpringPluginConfiguration;
import java.lang.annotation.*;
import org.springframework.context.annotation.Import;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(SpringPluginConfiguration.class)
public @interface EntryPoint {}
