package io.sunshower.kernel.spring;

import io.sunshower.kernel.api.Fulfillment;

public class TestExtensionPointServices implements TestExtensionPoint {

  @Override
  public String sayHello() {
    return "Hello";
  }
}
