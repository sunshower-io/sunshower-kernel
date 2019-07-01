package io.sunshower;

import java.lang.annotation.*;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.interceptor.InterceptorBinding;

/** Entry point to a Sunshower.io plugin */
@Startup
@Inherited
@Documented
@Singleton
@InterceptorBinding
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EntryPoint {}
