package io.sunshower.kernel.spring;

import io.sunshower.kernel.api.ExtensionPoint;

@ExtensionPoint(name = "ep")
public interface TestExtensionPoint {

  String sayHello();
}
