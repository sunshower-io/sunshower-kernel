package io.sunshower.kernel.wildfly;

import io.sunshower.kernel.api.Event;
import io.sunshower.kernel.api.Plugin;
import io.sunshower.kernel.api.PluginManager;
import io.sunshower.kernel.api.PluginNotFoundException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Singleton
@EJB(name = SunshowerKernelPluginManager.Name, beanInterface = PluginManager.class)
public class SunshowerKernelPluginManager implements PluginManager, Serializable {

    public static final String Name = "java:global/sunshower/kernel/plugin-manager";


    @Override
    public Plugin lookup(Plugin.Coordinate coordinate) {
        return null;
    }

    @Override
    public <T> void dispatchAll(Event<T> event, Event.Mode mode) {
        for (Plugin plugin : list()) {
            plugin.dispatch(event, mode);
        }
    }


    @Override
    public <T> void dispatch(Event<T> event, Event.Mode mode, Plugin.Coordinate... targets) {
        if (targets == null || targets.length == 0) {
            log.warn("Attempting to dispatch an event to the empty set.  Not doing anything");
        }
        for (Plugin.Coordinate coordinate : targets) {
            lookup(coordinate).dispatch(event, mode);
        }
    }

    @Override
    public List<Plugin> list() {
        return null;
    }

    @Override
    public List<Plugin> list(List<Plugin.Coordinate> items) throws PluginNotFoundException {
        return null;
    }

    @Override
    public Path getPluginDirectory() {
        return Path.of(System.getProperty("jboss.server.home.dir"), "deploy");
    }

    @Override
    public Path getPluginDirectory(Plugin.Coordinate coordinate) {
        return null;
    }

    @Override
    public void rescan() {
        val managementServer = resolveManagementServer();
    }

    @PostConstruct
    public void loadInitialPlugins() {
        rescan();
    }

    private MBeanServer resolveManagementServer() {
        return MBeanServerFactory.findMBeanServer(null).get(0);
    }
}
