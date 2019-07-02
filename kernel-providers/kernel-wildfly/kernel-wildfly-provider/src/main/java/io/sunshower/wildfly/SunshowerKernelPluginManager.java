package io.sunshower.wildfly;

import static java.lang.String.format;

import io.sunshower.EntryPoint;
import io.sunshower.api.*;
import io.sunshower.spi.PluginRegistrar;
import java.io.File;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.inject.Named;
import javax.management.*;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@Slf4j
@Primary
@EntryPoint
@Singleton
@Configuration
@Named("kernelPluginManager")
@Import(SunshowerKernelConfiguration.class)
@EJB(name = SunshowerKernelPluginManager.Name, beanInterface = PluginManager.class)
public class SunshowerKernelPluginManager implements PluginManager, Serializable {

  public static final String Name = "java:global/sunshower/kernel/plugin-manager";

  private final Map<PluginCoordinate, Plugin> plugins;

  public SunshowerKernelPluginManager() {
    plugins = new LinkedHashMap<>();
  }

  @Override
  public void waitForStartup() {
    log.info("Waiting for full plugin startup...");
    while (list().size() < pendingDeploymentCount()) {
      log.info("Waiting for plugins to start ({} of {})", list().size(), pendingDeploymentCount());
      try {
        Thread.sleep(1000);
      } catch (InterruptedException ex) {

      }
    }
  }

  @Override
  public int pendingDeploymentCount() {
    try {
      val server = resolveManagementServer();
      val oname = new ObjectName("jboss.as.expr:deployment=*");
      val ns = server.queryNames(oname, null);
      return ns.size();
    } catch (MalformedObjectNameException e) {
      throw new PluginException(e.getMessage());
    }
  }

  @Override
  public Plugin lookup(PluginCoordinate coordinate) {
    return plugins.get(coordinate);
  }

  @Override
  public <T> void dispatchAll(Event<T> event, Event.Mode mode) {
    for (Plugin plugin : list()) {
      plugin.dispatch(event, mode);
    }
  }

  @Override
  public <T> void dispatch(Event<T> event, Event.Mode mode, PluginCoordinate... targets) {
    if (targets == null || targets.length == 0) {
      log.warn("Attempting to dispatch an event to the empty set.  Not doing anything");
    }
    for (PluginCoordinate coordinate : targets) {
      lookup(coordinate).dispatch(event, mode);
    }
  }

  @Override
  public List<Plugin> list() {
    return List.copyOf(plugins.values());
  }

  @Override
  public List<Plugin> list(List<PluginCoordinate> items) throws PluginNotFoundException {
    return items.stream().map(this::lookup).collect(Collectors.toList());
  }

  @Override
  public Path getPluginDirectory() {
    String basedir = System.getProperty("jboss.server.base.dir");
    if (basedir != null) {
      return Path.of(basedir, "deployments");
    }
    log.warn("No basedir found");
    return null;
  }

  @Override
  public Path getDataDirectory() {
    log.info("Looking for data directory in SUNSHOWER_HOME...");
    var datadir = System.getenv("SUNSHOWER_HOME");
    if (checkDirectory(datadir)) {
      log.info("Resolved SUNSHOWER_HOME environment variable: {}", datadir);
      return verifyRoot(datadir, "kernel/data");
    }
    datadir = System.getProperty("sunshower.home");

    log.info("Looking for data directory in sunshower.home...");
    if (checkDirectory(datadir)) {
      log.info("Resolved sunshower.home system property: {}", datadir);
      return verifyRoot(datadir, "kernel/data");
    }
    throw new PluginException("Error: Plugin Directory not found");
  }

  @Override
  public Path getDataDirectory(PluginCoordinate coordinate) {
    val datadir = getDataDirectory().getParent().getParent();
    val file = datadir.resolve(coordinate.getPath()).toFile().getAbsolutePath();
    if (!checkDirectory(file)) {
      throw new PluginException("Error: Plugin directory not found--system is misconfigured");
    }
    return Path.of(file);
  }

  // eh, do we need this?
  @Override
  public Path getPluginDirectory(PluginCoordinate coordinate) {
    return getDataDirectory(coordinate);
  }

  @Override
  public void rescan() {
    log.info("Running rescan of plugins");
    val managementServer = resolveManagementServer();
    try {
      val scanner = new ObjectName("jboss.as:subsystem=deployment-scanner,scanner=default");
      managementServer.invoke(scanner, "runScan", null, null);
    } catch (Exception e) {
      throw new IllegalStateException("Can't resolve deployment scanner ID");
    }
  }

  @Override
  public void register(Plugin plugin) {
    check(plugin);
    log.info("Registering plugin: {}", plugin.getCoordinate());
    plugins.put(plugin.getCoordinate(), plugin);
  }

  @Override
  public String getNativeId(PluginCoordinate coordinate) {
    return lookup(coordinate).getNativeId();
  }

  @Override
  public Plugin.State getState(PluginCoordinate coordinate) {
    val server = resolveManagementServer();
    try {
      val sname = new ObjectName(format("jboss.as.expr:deployment=%s", getNativeId(coordinate)));
      val attr = (String) server.getAttribute(sname, "status");
      switch (attr) {
        case "OK":
          return Plugin.State.Running;
        case "FAILED":
          return Plugin.State.Failed;
        case "STOPPED":
          return Plugin.State.Stopped;
      }
      log.warn("Unknown state {}.  Defaulting to State=Unknown", attr);
    } catch (Exception e) {
      log.warn("error looking up status:", e);
    }
    return Plugin.State.Unknown;
  }

  private void check(Plugin plugin) {
    log.info("Enforcing deployer checks for plugin: {}", plugin.getCoordinate());
    verifyKnownRegistrars(plugin.getRegistrar(), plugin.getCoordinate());
  }

  private void verifyKnownRegistrars(PluginRegistrar registrar, PluginCoordinate coordinate) {
    log.info(
        "Verifying plugin registrar {} ({}) against known registrars...", registrar, coordinate);
    val registrars = ServiceLoader.load(PluginRegistrar.class);
    val itr = registrars.iterator();
    while (itr.hasNext()) {
      val knownRegistrar = itr.next();
      log.info("Checking registrar: {}", knownRegistrar);
      if (knownRegistrar.getClass().equals(registrar.getClass())) {
        log.info("Found registrar match: {}.  Checking protection domain...", knownRegistrar);
        val kernelProtectionDomain = knownRegistrar.getClass().getProtectionDomain();
        val pluginProtectionDomain = registrar.getClass().getProtectionDomain();
        if (!kernelProtectionDomain.equals(pluginProtectionDomain)) {
          log.warn("Plugin registrar matched, but protection domain did not.  Will not install");
        } else {
          log.info("Plugin passed load-time registration checks");
          return;
        }
      }
    }
    log.warn(
        format(
            "Warning: plugin %s did not pass all checks.  Reason: no appropriate plugin registrars installed.  "
                + "Please have your administrator install an appropriate, verified kernel module",
            coordinate));
  }

  private MBeanServer resolveManagementServer() {
    return ManagementFactory.getPlatformMBeanServer();
  }

  private Path verifyRoot(String datadir, String s) {
    val path = Path.of(datadir, s);
    val pfile = path.toFile();
    if (pfile.exists() && pfile.isDirectory()) {
      return path;
    }
    if (pfile.mkdirs()) {
      return path;
    }
    throw new PluginException(
        format("Directory %s was not found.  System is misconfigured", Path.of(datadir, s)));
  }

  private boolean checkDirectory(String dir) {
    log.info("Checking directory: {}", dir);
    if (!(dir == null || dir.isBlank())) {
      val f = new File(dir);
      if (f.exists() && f.isDirectory()) {
        return true;
      }
      return f.mkdirs();
    }
    return false;
  }
}
