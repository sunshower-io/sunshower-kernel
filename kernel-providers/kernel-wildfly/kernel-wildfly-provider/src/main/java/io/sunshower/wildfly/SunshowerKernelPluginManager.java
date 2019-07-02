package io.sunshower.wildfly;

import static java.lang.String.format;

import io.sunshower.EntryPoint;
import io.sunshower.api.*;
import io.sunshower.spi.PluginRegistrar;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.nio.file.Path;
import java.util.*;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.management.*;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Configuration;

@Slf4j
@EntryPoint
@Singleton
@Configuration
@EJB(name = SunshowerKernelPluginManager.Name, beanInterface = PluginManager.class)
public class SunshowerKernelPluginManager implements PluginManager, Serializable {

  public static final String Name = "java:global/sunshower/kernel/plugin-manager";

  @Override
  public Plugin lookup(PluginCoordinate coordinate) {
    return null;
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

    //    val loaders = getLoaders();
    //
    //    try {
    //      System.out.println("list");
    //      val server = resolveManagementServer();
    //      showLoaders(server);
    //      val oname = new ObjectName("jboss.as.expr:deployment=*");
    //      val ns = server.queryNames(oname, null);
    //      for (val n : ns) {
    //        val deployments = server.queryNames(n, null);
    //        for (val deployment : deployments) {
    //          val mbeaninfo = server.getMBeanInfo(deployment);
    //          for (val attr : mbeaninfo.getAttributes()) {
    //            val value = server.getAttribute(deployment, attr.getName());
    //            if (value instanceof CompositeData[]) {
    //              val datas = (CompositeData[]) value;
    //              for (val data : datas) {
    //                for (val vdata : data.values()) {
    //
    //                  System.out.format("\t%s -> %s\n", attr.getName(), vdata);
    //                }
    //              }
    //              //                            for(val v : ((CompositeData) value).values()) {
    //
    //              //                            }
    //
    //            } else {
    //              System.out.format("%s -> %s\n", attr.getName(), value);
    //            }
    //            //                    System.out.println(attr.getName());
    //            //                    System.out.println(server.invoke(deployment, attr.getName(),
    // null,
    //            // null));
    //          }
    //
    //          System.out.println("ops");
    //          for (val op : mbeaninfo.getOperations()) {
    //            System.out.println(op.getName());
    //          }
    //        }
    //        //            System.out.println(n.getKeyPropertyList());
    //      }
    //    } catch (Exception ex) {
    //      ex.printStackTrace();
    //    }
    return Collections.emptyList();
  }

  private void showLoaders(MBeanServer server) {
    //    val m = Module.forClassLoader(getClass().getClassLoader().getParent(), true);
    //    System.out.println("GOT" + m);
    //
    //    ////        for(ModuleLoader m = Module.getCallerModuleLoader(); m !=
    //    // Module.getBootModuleLoader();) {
    //    ////        val m = Module.getBootModuleLoader();
    //    //            System.out.println("Loading module: " + m);
    //    //            try {
    //    ////                m.loadModule("deployment.kernel-test-war3.war");
    //    //
    //    ////                val mods = m.iterateModules("deployment", true);
    //    ////                while(mods.hasNext()) {
    //    ////                    System.out.println(mods.next());
    //    ////                }
    //    //            } catch(Exception ex) {
    //    //                ex.printStackTrace();
    //    //
    //    //            }
    //
    //    //        }
    //    //        try {
    //    //            val names = new ObjectName("jboss.modules:type=ModuleLoader,name=*");
    //    //            val objs  = server.queryMBeans(names, null);
    //    //            for (val ob : objs) {
    //    //                val obj = server.getMBeanInfo(ob.getObjectName());
    //    //                System.out.println(ob.getObjectName());
    //    ////                if
    // (ob.getObjectName().getCanonicalName().contains("ServiceModuleLoader")) {
    //    //                if(true) {
    //    ////
    //    ////                    for (val attrs : obj.getAttributes()) {
    //    ////                        System.out.format("\t%s -> %s\n", attrs.getName(),
    // attrs.getType());
    //    ////                    }
    //    ////                    for (val attrs : obj.getOperations()) {
    //    ////                        System.out.format("\t%s -> %s\n", attrs.getName(),
    //    // attrs.getReturnType());
    //    ////                    }
    //    //
    //    //                    val moduleNames = (String[]) server.invoke(ob.getObjectName(),
    //    // "queryLoadedModuleNames", null, null);
    //    //                    for (val mname : moduleNames) {
    //    //                        System.out.println("\t\t" + mname);
    //    //                    }
    //    //                }
    //    ////                System.out.println(ob.getObjectName());
    //    ////                val moduleNames = (String[]) server.invoke(ob.getObjectName(),
    //    // "queryLoadedModuleNames", null, null);
    //    ////                for(val mname : moduleNames) {
    //    ////                    System.out.println(mname);
    //    ////                }
    //    ////                for (val attrs : obj.getAttributes()) {
    //    ////                    System.out.format("\t%s -> %s\n", attrs.getName(),
    // attrs.getType());
    //    ////                }
    //    ////                for (val attrs : obj.getOperations()) {
    //    ////                    System.out.format("\t%s -> %s\n", attrs.getName(),
    //    // attrs.getReturnType());
    //    ////                }
    //    ////
    //    ////                val loaders = (CompositeData[]) server.invoke(ob.getObjectName(),
    //    // "getResourceLoaders", null, null);
    //    ////                for(val loader : loaders) {
    //    ////                    System.out.println(loader);
    //    ////                }
    //    //
    //    //
    //    //            }
    //    //
    //    //        } catch (Exception ex) {
    //    //            ex.printStackTrace();
    //    //            ;
    //    //        }
  }

  @Override
  public List<Plugin> list(List<PluginCoordinate> items) throws PluginNotFoundException {
    return null;
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
  public Path getPluginDirectory(PluginCoordinate coordinate) {
    return null;
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
  }

  private void check(Plugin plugin) {
    log.info("Enforcing deployer checks for plugin: {}", plugin.getCoordinate());
    //    verifyKnownRegistrars(plugin.getRegistrar(), plugin.getCoordinate());
  }

  private void verifyKnownRegistrars(PluginRegistrar registrar, PluginCoordinate coordinate) {
    log.info("Verifying plugin registrar {} against known registrars...");
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
    throw new PluginException(
        format(
            "Will not deploy plugin %s.  Reason: no appropriar plugin registrars installed.  "
                + "Please have your administrator install an appropriate kernel module",
            coordinate));
  }

  private MBeanServer resolveManagementServer() {
    return ManagementFactory.getPlatformMBeanServer();
    //        return MBeanServerFactory.findMBeanServer(null).get(0);
  }
}
