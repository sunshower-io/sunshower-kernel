package io.sunshower.testplugins;

import io.sunshower.EntryPoint;
import io.sunshower.api.PluginManager;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@EntryPoint
public class SimpleTestPlugin {

  @Resource(name = "java:global/sunshower/kernel/plugin-manager")
  private PluginManager pluginManager;

  @PostConstruct
  public void start() {
    System.out.println("START");
    try {
      pluginManager.start(SimpleTestPlugin.class);
      System.out.println("DONE");
    } catch (Exception ex) {
      System.out.println("done");
    }
  }
}
