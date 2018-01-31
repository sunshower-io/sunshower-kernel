package io.sunshower.kernel.wildfly;

import javax.ejb.Singleton;

@Singleton
public class Plugins {

  public static final class DefaultNamespaces {
    public static final String SUNSHOWER = "sunshower";
    public static final String PLUGIN_MANAGER = "java:global/sunshower/kernel/plugin-manager";
  }
  

  public static final class DefaultGroups {
    public static final String ADMINISTRATIVE = "administrative";
  }

}
