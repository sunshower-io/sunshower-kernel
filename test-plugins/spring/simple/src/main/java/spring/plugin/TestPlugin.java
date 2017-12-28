package spring.plugin;

import io.sunshower.kernel.api.OnStart;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import test.plugin.TestExtension;

import java.util.List;

public class TestPlugin extends Plugin {

    public TestPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }
    
    @OnStart
    public void onStart() {
        List<TestExtension> extensions = this.getWrapper().getPluginManager().getExtensions(TestExtension.class);
        extensions.forEach(TestExtension::sayHelloFromOtherPlugin);
    }
}