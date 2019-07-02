package io.sunshower.testplugins;

import io.sunshower.EntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EntryPoint
@Configuration
public class SimpleTestPlugin {

  //  @Inject private PluginManager pluginManager;

  public SimpleTestPlugin() {
    System.out.println("GOT ONE");
  }

  @Bean
  public Test test() {
    return new Test();
  }

  public static class Test {
    public void whatever() {
      System.out.println("Whatevs!2");
    }
  }
}
