package io.sunshower.kernel;

import io.sunshower.kernel.configuration.PluginConfiguration;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import javax.inject.Inject;
import javax.inject.Named;

class TestPlugin extends Plugin {
   
    @Inject
    private PluginConfiguration configuration;
    
    @Inject
    private String name;

    @Inject
    @Named("hello-name")
    private String helloName;
    
    public TestPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    public String getName() {
        return name;
    }

    public String getHelloName() {
        return helloName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHelloName(String helloName) {
        this.helloName = helloName;
    }

    public PluginConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(PluginConfiguration configuration) {
        this.configuration = configuration;
    }
}
