package io.sunshower.kernel.spring;

public class TestExtensionPointServices implements TestExtensionPoint {

  @Override
  public String sayHello() {
    return "Hello";
  }
}
