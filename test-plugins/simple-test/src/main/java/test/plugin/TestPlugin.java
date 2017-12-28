package test.plugin;

import io.sunshower.kernel.api.OnLoad;
import io.sunshower.kernel.api.OnStart;
import io.sunshower.kernel.api.OnStop;
import io.sunshower.kernel.api.OnUnload;
import io.sunshower.kernel.test.LifecycleExposedPlugin;
import io.sunshower.kernel.test.PluginStatus;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

public class TestPlugin extends Plugin implements LifecycleExposedPlugin {

    volatile PluginStatus status;
    
    
    public TestPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }
   
    @OnLoad
    public void onLoad() {
        status = PluginStatus.Loaded;
        System.out.println("loaded!");
        
    }
   
    @OnStart
    public void onStart() {
        status = PluginStatus.Started;
        System.out.println("started!");
    }
   
    @OnStop
    public void onStop() {
        status = PluginStatus.Stopped;
        System.out.println("Stopped!");
    }
    
    @OnUnload
    public void onUnload() {
        status = PluginStatus.Unloaded;
        System.out.println("Unloaded!");
    }

    @Extension
    public static class TestExtension {
        
        public TestExtension() {
        }

    }

    @Override
    public PluginStatus getState() {
        return status;
    }
}
