package io.sunshower.kernel.api;

import javax.annotation.Nonnull;
import javax.annotation.Order;
import javax.annotation.Ordered;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Set;

public interface Plugin extends Serializable {

  @Nonnull
  String getVersion();

  PluginState getState();

  void stop();

  void start();

  <T> T fulfill(FulfillmentDefinition<T> definition);

  <T> T export(FulfillmentDefinition<T> definition);

  <T> T fulfill(Class<T> type, String mappedName);

  void exportAll();

  <T> T fulfill(Class<T> type);

  @Nonnull
  String getName();

  @Nonnull
  String getGroup();

  @Nonnull
  String getNamespace();

  @Nonnull
  Set<String> getPackagesForComponents();

  @Nonnull
  <T> Set<FulfillmentDefinition<T>> getFulfillmentDefinitionsFor(Class<T> type);

  @Nonnull
  <T> FulfillmentDefinition<T> getFulfillmentDefinition(Class<T> type, String name);

  @Nonnull
  ClassLoader getPluginClassLoader();

  @Nonnull
  URL resolveUrl(String path) throws FileNotFoundException;

  @Nonnull
  InputStream resolveInputStream(String path);

  @Ordered(Order.DESCENDING)
  Set<ExtensionPointDefinition<?>> getExtensionPoints();

  @Ordered(Order.DESCENDING)
  Set<FulfillmentDefinition<?>> getExtensionFulfillments();
}
