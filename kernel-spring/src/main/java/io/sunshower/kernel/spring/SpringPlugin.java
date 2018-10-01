package io.sunshower.kernel.spring;

import io.sunshower.kernel.api.*;
import io.sunshower.kernel.api.PluginExporter;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.context.ApplicationContext;

public class SpringPlugin implements Plugin {

  final NamingStrategy namingStrategy;
  final ExtensionCoordinate coordinate;
  final PluginExporter exporter;
  final ApplicationContext pluginContext;
  final AnnotationMetadataScanner scanner;

  public SpringPlugin(
      ExtensionCoordinate coordinate,
      ApplicationContext pluginContext,
      NamingStrategy namingStrategy,
      PluginExporter pluginExporter) {

    this.coordinate = coordinate;
    this.exporter = pluginExporter;
    this.pluginContext = pluginContext;
    this.namingStrategy = namingStrategy;
    this.scanner = new AnnotationMetadataScanner(pluginContext, this);
  }

  @Nonnull
  @Override
  public String getVersion() {
    return coordinate.getVersion();
  }

  @Override
  public PluginState getState() {
    return null;
  }

  @Override
  @PreDestroy
  public void stop() {}

  @Override
  @PostConstruct
  public void start() {
    exporter.export(this);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T fulfill(FulfillmentDefinition<T> definition) {
    return (T) pluginContext.getBean(definition.getMappedName());
  }

  @Override
  public <T> T export(FulfillmentDefinition<T> definition) {
    final T fulfillment = fulfill(definition);
    if (Serializable.class.isAssignableFrom(definition.getExtensionPoint())) {
      exporter.export(definition, this, fulfillment);
    } else {
      final JNDIPluginProxyInvocationHandler<T> proxy =
          new JNDIPluginProxyInvocationHandler<>(
              namingStrategy, definition.getExtensionPoint(), fulfillment, this);
      final T proxiedFulfillment = proxy.proxy();
      exporter.export(definition, this, proxiedFulfillment);
      return proxiedFulfillment;
    }
    return fulfillment;
  }

  @Override
  public <T> T fulfill(Class<T> type, String mappedName) {
    return pluginContext.getBean(mappedName, type);
  }

  @Override
  public void exportAll() {
    for (FulfillmentDefinition<?> definition : getExtensionFulfillments()) {
      export(definition);
    }
  }

  @Override
  public <T> T fulfill(Class<T> type) {
    return pluginContext.getBean(type);
  }

  @Nonnull
  @Override
  public String getName() {
    return coordinate.getName();
  }

  @Nonnull
  @Override
  public String getGroup() {
    return coordinate.getGroup();
  }

  @Nonnull
  @Override
  public String getNamespace() {
    return coordinate.getNamespace();
  }

  @Nonnull
  @Override
  public Set<String> getPackagesForComponents() {
    return Collections.emptySet();
  }

  @Nonnull
  @Override
  @SuppressWarnings("unchecked")
  public <T> Set<FulfillmentDefinition<T>> getFulfillmentDefinitionsFor(Class<T> type) {
    return scanner
        .getFulfillments()
        .stream()
        .filter(t -> type.isAssignableFrom(t.getExtensionPoint()))
        .map(t -> (FulfillmentDefinition<T>) t)
        .collect(Collectors.toSet());
  }

  @Nonnull
  @Override
  @SuppressWarnings("unchecked")
  public <T> FulfillmentDefinition<T> getFulfillmentDefinition(Class<T> type, String name) {
    final Optional<FulfillmentDefinition<?>> first =
        scanner
            .getFulfillments()
            .stream()
            .filter(
                t -> t.getMappedName().equals(name) && type.isAssignableFrom(t.getExtensionPoint()))
            .findFirst();
    return (FulfillmentDefinition<T>) first.get();
  }

  @Nonnull
  @Override
  public ClassLoader getPluginClassLoader() {
    ClassLoader cl = pluginContext.getClassLoader();
    if (cl == null) {
      return Thread.currentThread().getContextClassLoader();
    }
    return cl;
  }

  @Nonnull
  @Override
  public URL resolveUrl(String path) throws FileNotFoundException {
    return getPluginClassLoader().getResource(path);
  }

  @Nonnull
  @Override
  public InputStream resolveInputStream(String path) {
    return getPluginClassLoader().getResourceAsStream(path);
  }

  @Override
  public Set<ExtensionPointDefinition<?>> getExtensionPoints() {
    return scanner.getExtensionPoints();
  }

  @Override
  public Set<FulfillmentDefinition<?>> getExtensionFulfillments() {
    return scanner.getFulfillments();
  }
}
