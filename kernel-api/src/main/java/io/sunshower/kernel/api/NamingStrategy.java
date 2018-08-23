package io.sunshower.kernel.api;

public interface NamingStrategy {

  String nameFor(ExtensionCoordinate coordinate, Plugin plugin);
}
