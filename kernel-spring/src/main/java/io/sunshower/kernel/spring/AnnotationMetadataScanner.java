package io.sunshower.kernel.spring;

import io.sunshower.kernel.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.*;

public class AnnotationMetadataScanner {

  static final Logger logger = LoggerFactory.getLogger(AnnotationMetadataScanner.class);

  private final Object fulfillmentDefinitionLock = new Object();
  private final Object extensionPointDefinitionLock = new Object();

  private final Plugin plugin;
  private final ConfigurableApplicationContext applicationContext;

  private Map<ExtensionCoordinate, FulfillmentDefinition<?>> fulfillmentDefinitions;
  private Map<ExtensionCoordinate, ExtensionPointDefinition<?>> extensionPointDefinitions;

  public AnnotationMetadataScanner(ApplicationContext applicationContext, Plugin plugin) {
    this.plugin = plugin;
    this.applicationContext = (ConfigurableApplicationContext) applicationContext;
  }

  public Set<ExtensionPointDefinition<?>> getExtensionPoints() {
    if (extensionPointDefinitions == null) {
      synchronized (extensionPointDefinitionLock) {
        if (extensionPointDefinitions == null) {
          loadExtensionPointDefinitions();
          return new LinkedHashSet<>(extensionPointDefinitions.values());
        }
      }
    }
    return new LinkedHashSet<>(extensionPointDefinitions.values());
  }

  private void loadExtensionPointDefinitions() {
    extensionPointDefinitions = new LinkedHashMap<>();
    ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
    scanMethodsForExtensionPoints(beanFactory);
    scanClassDefinitionsForExtensionPoints(beanFactory);
  }

  private void register(String name) {
    final ExtensionCoordinate coordinate =
        new ExtensionCoordinate(
            plugin.getGroup(), plugin.getNamespace(), name, plugin.getVersion());
    final ExtensionPointDefinition<?> pointDefinition =
        new SpringExtensionPointDefinition<>(applicationContext, coordinate, plugin);
    extensionPointDefinitions.put(coordinate, pointDefinition);
  }

  private void scanClassDefinitionsForExtensionPoints(ConfigurableListableBeanFactory beanFactory) {
    final String[] names = applicationContext.getBeanNamesForAnnotation(ExtensionPoint.class);
    for (String name : names) {
      register(name);
    }
  }

  public Set<FulfillmentDefinition<?>> getFulfillments() {
    if (fulfillmentDefinitions == null) {
      synchronized (fulfillmentDefinitionLock) {
        if (fulfillmentDefinitions == null) {
          fulfillmentDefinitions = new LinkedHashMap<>();
          final ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
          scanMethodsForFulfillments(beanFactory);
          scanClassDefinitionsForFulfillments(beanFactory);
        }
      }
    }
    return new LinkedHashSet<>(fulfillmentDefinitions.values());
  }

  private void scanClassDefinitionsForFulfillments(ConfigurableListableBeanFactory beanFactory) {
    final String[] names = applicationContext.getBeanNamesForAnnotation(Fulfillment.class);
    for (String name : names) {
      registerFulfillment(name, beanFactory, beanFactory.getMergedBeanDefinition(name), null);
    }
  }

  private void registerFulfillment(
      String name,
      ConfigurableListableBeanFactory beanFactory,
      BeanDefinition mergedBeanDefinition,
      AnnotatedTypeMetadata metadata) {
    final Class<?> type = resolveActualTypeOf(mergedBeanDefinition, metadata);
    final ExtensionCoordinate coordinate =
        new ExtensionCoordinate(
            plugin.getGroup(), plugin.getNamespace(), name, plugin.getVersion());
    final FulfillmentDefinition<?> definition =
        new SpringFulfillmentDefinition<>(coordinate, name, type);
    fulfillmentDefinitions.put(coordinate, definition);
  }

  private Class<?> resolveActualTypeOf(
      BeanDefinition mergedBeanDefinition, AnnotatedTypeMetadata metadata) {
    if (metadata == null) {
      final Object source = mergedBeanDefinition.getSource();
      if (source instanceof AnnotatedTypeMetadata) {
        metadata = (AnnotatedTypeMetadata) source;
      }
    }

    if (metadata == null && mergedBeanDefinition instanceof RootBeanDefinition) {
      final Class<?> type = ((RootBeanDefinition) mergedBeanDefinition).getTargetType();
      if (type != null && type.isAnnotationPresent(Fulfillment.class)) {
        final Fulfillment annotation = type.getAnnotation(Fulfillment.class);
        return annotation.extensionPoint();
      }
      return type;
    }

    Map<String, Object> attributes = metadata.getAnnotationAttributes(Fulfillment.class.getName());
    return (Class) attributes.get("fulfillmentType");
  }

  private void scanMethodsForFulfillments(ConfigurableListableBeanFactory factory) {
    final String[] names = factory.getBeanDefinitionNames();
    for (String name : names) {
      final BeanDefinition definition = factory.getBeanDefinition(name);
      final Object source = definition.getSource();
      if (source instanceof AnnotatedTypeMetadata) {
        AnnotatedTypeMetadata metadata = (AnnotatedTypeMetadata) source;
        Map<String, Object> attributes =
            metadata.getAnnotationAttributes(Fulfillment.class.getName());
        logger.info("Located extension point fulfillment named {}", name);
        if (attributes != null) {
          registerFulfillment(name, factory, factory.getMergedBeanDefinition(name), metadata);
        }
      }
    }
  }

  private void scanMethodsForExtensionPoints(ConfigurableListableBeanFactory factory) {
    final String[] names = factory.getBeanDefinitionNames();
    for (String name : names) {
      final BeanDefinition definition = factory.getBeanDefinition(name);
      final Object source = definition.getSource();
      if (source instanceof AnnotatedTypeMetadata) {
        AnnotatedTypeMetadata metadata = (AnnotatedTypeMetadata) source;
        Map<String, Object> attributes =
            metadata.getAnnotationAttributes(ExtensionPoint.class.getName());
        logger.info("Located extension point named {}", name);
        if (attributes != null) {
          register(name);
        }
      }
    }
  }
}
