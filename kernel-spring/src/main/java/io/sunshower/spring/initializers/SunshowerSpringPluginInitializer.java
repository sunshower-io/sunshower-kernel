package io.sunshower.spring.initializers;

import io.github.classgraph.ClassGraph;
import io.sunshower.EntryPoint;
import io.sunshower.api.PluginCoordinate;
import io.sunshower.api.PluginException;
import io.sunshower.api.PluginManager;
import io.sunshower.kernel.core.PluginScanner;
import io.sunshower.spring.processors.SpringPluginLifecycle;
import java.io.IOException;
import java.util.Set;
import java.util.jar.Manifest;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

@Slf4j
public class SunshowerSpringPluginInitializer implements ServletContainerInitializer {

  static final String ROOT_APPLICATION_CONTEXT = "sunshower::root::application::context";

  @Override
  public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
    log.info("Sunshower Kernel is attempting to start plugin: {}", ctx.getClassLoader());
    doStart(ctx);
  }

  private void doStart(ServletContext ctx) {

    val pluginManager = getPluginManager();
    log.info("Attempting to locate plugin entry point...");
    val classloader = ctx.getClassLoader();
    val entryPoint = scan(classloader);
    if (entryPoint != null) {
      try {
        log.info("Located entry point: {}...attempting to start", entryPoint);
        val context = new AnnotationConfigWebApplicationContext();
        context.setClassLoader(ctx.getClassLoader());
        SpringPluginLifecycle.setServletContext(ctx);
        SpringPluginLifecycle.setEntryPoint(entryPoint);
        SpringPluginLifecycle.setPluginManager(pluginManager);
        SpringPluginLifecycle.setPluginClassloader(ctx.getClassLoader());
        SpringPluginLifecycle.setCoordinate(scan(classloader, entryPoint));
        if (ctx.getAttribute(ROOT_APPLICATION_CONTEXT) == null) {
          log.info("No root application found.  Registering...");
          ctx.setAttribute(ROOT_APPLICATION_CONTEXT, new Object());
          ctx.addListener(new ContextLoaderListener(context));
          log.info("Successfully registered root application context");
        }
        log.info("Registering entry point: {}", entryPoint);
        context.register(entryPoint);

      } catch (Exception e) {
        log.error("Encountered exception {} while attempting to register plugin", e.getMessage());
      }
    }
  }

  private PluginCoordinate scan(ClassLoader classloader, Class<?> entryPoint) {
    val coordinate =
        PluginScanner.getDefaultChain()
            .scan(new PluginScanner.PluginContext(entryPoint, classloader));
    if (coordinate.isPresent()) {
      return coordinate.get();
    }
    log.warn("Failed to locate plugin coordinate.");
    throw new PluginException("Failed to locate plugin coordinate");
  }

  private PluginManager getPluginManager() {
    try {
      return InitialContext.doLookup("java:global/sunshower/kernel/plugin-manager");
    } catch (NamingException e) {
      throw new PluginException("Failed to look up plugin manager. ", e);
    }
  }

  private Class<?> scan(ClassLoader classLoader) {
    Class<?> result = checkManifest(classLoader);
    if (result != null) {
      return result;
    }

    val annotatedClasses =
        new ClassGraph()
            .enableClassInfo()
            .enableAnnotationInfo()
            .overrideClassLoaders(classLoader)
            .ignoreParentClassLoaders()
            .ignoreParentModuleLayers()
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
    result = annotatedClasses.get(0).loadClass();
    log.info("Successfully loaded plugin entrypoint: {}", result);
    return result;
  }

  private Class<?> checkManifest(ClassLoader classLoader) {
    log.info("Scanning plugin manifest for entry-point...");
    try {
      val manifests = classLoader.getResources("META-INF/MANIFEST.MF");
      while (manifests.hasMoreElements()) {
        try (val stream = manifests.nextElement().openStream()) {
          val manifest = new Manifest(stream);
          val attrs = manifest.getMainAttributes();
          if (attrs.containsKey("entry-point")) {
            val entryPoint = attrs.getValue("entry-point");
            log.info("Found entry-point: {}", entryPoint);
            return Class.forName(entryPoint, true, classLoader);
          }
        } catch (ClassNotFoundException e) {
          log.warn("Error: entry-point in manifest was not found");
        }
      }
    } catch (IOException e) {
      log.warn("Failed to read manifests.  Reason: {}", e.getMessage());
    }
    log.info("No plugin manifests found.");
    return null;
  }
}
