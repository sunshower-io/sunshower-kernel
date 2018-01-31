package io.sunshower.kernel.api;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.atomic.AtomicReference;

@WebListener
public class ContextPathProvider implements ServletContextListener, ExtensionMetadata {
  private static final AtomicReference<String> contextPath = new AtomicReference<>();
  
  public ContextPathProvider() {
  }

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    contextPath.set(sce.getServletContext().getContextPath());
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {}

  public String getContextPath() {
    return contextPath.get();
  }

  @Override
  public String getDeploymentLocation() {
    return getContextPath();
  }
}
