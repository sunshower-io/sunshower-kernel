package io.sunshower.kernel.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class KernelSystemTest {
   
    @Deployment
    public static WebArchive webArchive() {
        return ShrinkWrap.create(WebArchive.class, "kernel-test.war")
                .addClass(KernelSystemTest.class);
    }
    
    @Test
    public void ensureTestsLoad() {
        
        
    }
}
