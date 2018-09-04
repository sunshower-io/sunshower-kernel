package io.sunshower.kernel.spring;

import io.sunshower.kernel.api.*;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jndi.JndiTemplate;

import javax.naming.NamingException;

@Getter
@Setter
public class JNDIPluginExporter<T> implements PluginExporter {

  static final Logger log = LoggerFactory.getLogger(JNDIPluginExporter.class);
  private final JndiTemplate template;
  private final NamingStrategy namingStrategy;

  public JNDIPluginExporter(JndiTemplate template, NamingStrategy namingStrategy) {
    this.template = template;
    this.namingStrategy = namingStrategy;
  }

  @Override
  public void export(Plugin p) {

    final String name =
        namingStrategy.nameFor(
            new ExtensionCoordinate(p.getGroup(), p.getNamespace(), p.getName(), p.getVersion()),
            p);

    try {
      template.rebind(name, p);
    } catch (NamingException e) {
      throw new PluginExportException(e);
    }
  }

  @Override
  public <T> void export(FulfillmentDefinition<T> definition, Plugin plugin, T fulfillment) {
    String jndiName = namingStrategy.nameFor(definition.getCoordinate(), plugin);
    try {
      template.rebind(jndiName, fulfillment);
    } catch (NamingException e) {
      throw new PluginExportException(e);
    }
  }

  @Override
  public <T> void destroy(FulfillmentDefinition<T> definition, Plugin plugin) {
    String jndiName = namingStrategy.nameFor(definition.getCoordinate(), plugin);
    try {
      template.unbind(jndiName);
    } catch (NamingException e) {
      throw new PluginExportException(e);
    }
  }
}
