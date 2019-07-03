package io.sunshower.spring;

import io.sunshower.api.*;
import io.sunshower.spi.PluginRegistrar;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Path;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.type.StandardMethodMetadata;

@Slf4j
public class SpringPlugin implements Plugin {

  private final ClassLoader classLoader;
  private final PluginRegistrar registrar;
  private final PluginManager pluginManager;
  private final PluginCoordinate coordinate;
  private final ServletContext servetContext;
  private final ProtectionDomain protectionDomain;
  private final LifecycleManager applicationContext;

  public SpringPlugin(
      ServletContext servletContext,
      PluginManager pluginManager,
      ClassLoader classLoader,
      PluginCoordinate coordinate,
      PluginRegistrar pluginRegistrar,
      ProtectionDomain protectionDomain,
      LifecycleManager applicationContext) {
    this.servetContext = servletContext;
    this.classLoader = classLoader;
    this.coordinate = coordinate;
    this.pluginManager = pluginManager;
    this.registrar = pluginRegistrar;
    this.protectionDomain = protectionDomain;
    this.applicationContext = applicationContext;
  }

  @Override
  public Icon getIcon() {
    try {

      val resources = classLoader.getResources("assets/plugin-icon.svg");
      while (resources.hasMoreElements()) {
        try (val iconStream = resources.nextElement().openStream()) {
          val bos = new ByteArrayOutputStream();
          val buffer = new byte[1024];
          while (iconStream.read(buffer) != -1) {
            bos.writeBytes(buffer);
          }
          return new Icon(bos.toByteArray(), "svg");
        }
      }
    } catch (Exception e) {
      log.warn("Error loading icon: {}", e.getMessage());
    }
    return null;
  }

  @Override
  public PluginRegistrar getRegistrar() {
    return registrar;
  }

  @Override
  public ProtectionDomain getProtectionDomain() {
    return protectionDomain;
  }

  @Override
  public String getNativeId() {
    return ((File) servetContext.getAttribute("javax.servlet.context.tempdir")).getName();
  }

  @Override
  public State getState() {
    return pluginManager.getState(coordinate);
  }

  @Override
  public PluginCoordinate getCoordinate() {
    return coordinate;
  }

  @Override
  public List<Class<?>> getExportedExtensionPoints() {
    val ctx = (ConfigurableApplicationContext) applicationContext.unwrap(ApplicationContext.class);
    val factory = ctx.getBeanFactory();
    val types = new ArrayList<Class<?>>();
    for (val name : factory.getBeanDefinitionNames()) {
      val definition = factory.getBeanDefinition(name);
      if (definition.getSource() instanceof StandardMethodMetadata) {
        val metadata = (StandardMethodMetadata) definition.getSource();
        val sourceType = metadata.getReturnTypeName();
        try {
          val type = Class.forName(sourceType, true, classLoader);
          if (type.isAnnotationPresent(ExtensionPoint.class)) {
            types.add(type);
          }
        } catch (ClassNotFoundException e) {
          log.warn(
              "Failed to instantiate extension-point {} of plugin {}.  Reason: {}",
              definition.getBeanClassName(),
              coordinate,
              e.getCause());
        }
      }
    }
    return types;
  }

  @Override
  public <T> T getExtensionPoint(Class<T> type) {
    return applicationContext.unwrap(ApplicationContext.class).getBean(type);
  }

  @Override
  public <T> boolean exportsExtensionPoint(Class<T> type) {
    return !applicationContext.unwrap(ApplicationContext.class).getBeansOfType(type).isEmpty();
  }

  @Override
  public ClassLoader getClassLoader() {
    return classLoader;
  }

  @Override
  public Path getPluginDirectory() {
    return pluginManager.getDataDirectory(coordinate);
  }

  @Override
  public void setConfiguration(Class<?> type, Object configuration) {
    applicationContext
        .unwrap(ApplicationContext.class)
        .publishEvent(new ConfigurationChangedEvent(configuration, type));
  }

  @Override
  public Object getConfiguration(Class<?> type) {
    return applicationContext
        .unwrap(ApplicationContext.class)
        .getBean(ConfigurationManager.class)
        .getConfiguration(type);
  }

  @Override
  public List<ConfigurationSet> getConfigurables() {
    return null;
  }

  @Override
  public List<ConfigurationSet> getConfigurables(String category) {
    return null;
  }

  @Override
  public Type getType() {
    return Type.Root;
  }

  @Override
  public String getContextPath() {
    return null;
  }

  @Override
  public void dispatch(Event event, Event.Mode mode) {}

  @Override
  public Resource getResource() {
    return null;
  }

  @Override
  public ClassLoader getClassloader() {
    return null;
  }
}
