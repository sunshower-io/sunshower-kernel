package io.sunshower.wildfly.model;

import lombok.val;

public class Models {

  public static DeploymentDescriptorElement parse(String key, String value) {
    if (key == null) {
      throw new IllegalArgumentException("Module key must not be null");
    }
    val idx = key.lastIndexOf('-');
    val name = key.substring(0, idx);
    val v = key.substring(idx + 1, key.lastIndexOf('.'));
    return new DeploymentDescriptorElement(name, v);
  }
}
