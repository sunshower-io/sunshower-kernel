package io.sunshower.api;

import lombok.Data;

@Data
public final class PluginCoordinate {

  private final String group;
  private final String name;
  private final String version;
  private final String hash;

  public String getPath() {
    return String.format("%s_%s_%s", group, name, version);
  }
}
