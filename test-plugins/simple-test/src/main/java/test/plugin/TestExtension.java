package test.plugin;

import io.sunshower.kernel.Plugin;
import org.pf4j.Extension;

@Extension
public class TestExtension implements Plugin {
    
    
    public void sayHelloFromOtherPlugin() {
        System.out.println("Hello from other plugin!");
    }
    
}
