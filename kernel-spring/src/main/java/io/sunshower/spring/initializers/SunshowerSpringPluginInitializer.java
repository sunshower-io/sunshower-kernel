package io.sunshower.spring.initializers;

import io.github.classgraph.ClassGraph;
import io.sunshower.EntryPoint;
import io.sunshower.api.PluginException;
import java.util.Set;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

@Slf4j
public class SunshowerSpringPluginInitializer implements ServletContainerInitializer {
  @Override
  public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
    log.info("Sunshower Kernel is attempting to start plugin: {}", ctx.getClassLoader());
    doStart(ctx);
  }

  private void doStart(ServletContext ctx) {
    log.info("Attempting to locate plugin entry point...");
    val classloader = ctx.getClassLoader();
    val entryPoint = scan(classloader);
    if (entryPoint != null) {
      log.info("Located entry point: {}...attempting to start", entryPoint);
      val context = new AnnotationConfigWebApplicationContext();
      context.register(entryPoint);
      ctx.addListener(new ContextLoaderListener(context));
    }
  }

  private Class<?> scan(ClassLoader classLoader) {
    val annotatedClasses =
        new ClassGraph()
            .enableClassInfo()
            .enableAnnotationInfo()
            .overrideClassLoaders(classLoader)
            .removeTemporaryFilesAfterScan()
            .scan()
            .getClassesWithAnnotation(EntryPoint.class.getCanonicalName());
    if (annotatedClasses.isEmpty()) {
      val msg =
          "No classes annotated with "
              + EntryPoint.class.getName()
              + " found in classloader "
              + classLoader.getName()
              + ".  Sunshower::Spring cannot handle this "
              + "(but some other Sunshower kernel provider might be able to)";
      log.info(msg);
      return null;
    }

    if (annotatedClasses.size() > 1) {
      val msg =
          "Identified more than one class annotated with @io.sunshower.EntryPoint.  "
              + "Sunshower requires exactly one class to be annotated with "
              + "@io.sunshower.EntryPoint.  Offending classes: "
              + annotatedClasses
              + ".  Failing deployment";
      log.error(msg);
      throw new PluginException(msg);
    }
    val result = annotatedClasses.get(0).loadClass();
    log.info("Successfully loaded plugin entrypoint: {}", result);
    return result;
  }
}
