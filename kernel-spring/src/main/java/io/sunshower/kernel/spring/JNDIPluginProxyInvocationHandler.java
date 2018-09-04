package io.sunshower.kernel.spring;

import io.sunshower.kernel.api.NamingStrategy;
import io.sunshower.kernel.api.Plugin;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JNDIPluginProxyInvocationHandler<T> implements InvocationHandler, Serializable {

  final Plugin plugin;
  final T serviceInstance;
  final Class<T> serviceInterface;
  final NamingStrategy namingStrategy;

  public JNDIPluginProxyInvocationHandler(
      NamingStrategy strategy, Class<T> iface, T instance, Plugin plugin) {
    this.plugin = plugin;
    this.serviceInstance = instance;
    this.serviceInterface = iface;
    this.namingStrategy = strategy;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    return method.invoke(serviceInstance, args);
  }

  @SuppressWarnings("unchecked")
  public T proxy() {
    return (T)
        Proxy.newProxyInstance(
            plugin.getPluginClassLoader(),
            new Class<?>[] {serviceInterface, Serializable.class},
            this);
  }
}
