package io.sunshower.kernel.testplugins2;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
public class TestPlugin2 {


    @PostConstruct
    public void startSpring() {
        final AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(TestPluginConfig.class);
        ctx.refresh();
    }



}
