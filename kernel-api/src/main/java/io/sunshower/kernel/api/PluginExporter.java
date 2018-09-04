package io.sunshower.kernel.api;

public interface PluginExporter {

  void export(Plugin p);

  <T> void export(FulfillmentDefinition<T> definition, Plugin plugin, T fulfillment);

  <T> void destroy(FulfillmentDefinition<T> definition, Plugin plugin);
}
