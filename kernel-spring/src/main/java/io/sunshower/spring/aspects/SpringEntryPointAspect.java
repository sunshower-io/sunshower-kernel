package io.sunshower.spring.aspects;

import lombok.val;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SpringEntryPointAspect {

  @Pointcut("within(@io.sunshower.EntryPoint *)")
  public void serviceAnnotatedWithEntryPoint() {}

  @Pointcut("execution(*.new(..))")
  public void invokeOnConstructor() {}

  @Pointcut("serviceAnnotatedWithEntryPoint() && invokeOnConstructor()")
  public Object registerPlugin(ProceedingJoinPoint joinPoint) throws Throwable {
    System.out.println("ZOMGZOMG");

    val result = joinPoint.proceed();
    System.out.println("DONNNNNNE");
    return result;
  }
}
