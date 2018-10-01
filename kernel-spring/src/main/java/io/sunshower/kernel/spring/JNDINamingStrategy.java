package io.sunshower.kernel.spring;

import io.sunshower.kernel.api.ExtensionCoordinate;
import io.sunshower.kernel.api.NamingStrategy;
import io.sunshower.kernel.api.Plugin;

public class JNDINamingStrategy implements NamingStrategy {

  @Override
  public String nameFor(ExtensionCoordinate coordinate, Plugin plugin) {
    String s =
        String.format(
            "java:global/%s/%s/%s_%s",
            plugin.getNamespace(),
            plugin.getGroup(),
            coordinate.getName(),
            normalize(plugin.getVersion()));
    System.out.println(s);
    return s;
  }

  String normalize(String n) {
    return n.replaceAll("[\\.,-]", "_");
  }
}
