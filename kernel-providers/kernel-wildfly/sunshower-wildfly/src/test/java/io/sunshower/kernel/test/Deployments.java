package io.sunshower.kernel.test;

import static io.sunshower.kernel.test.ManagementModelSystemTest.file;

import io.sunshower.test.common.TestClasspath;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class Deployments {

  @Deployment
  public static WebArchive baseDeployment() {
    return ShrinkWrap.create(WebArchive.class, "kernel-test-war3.war")
        .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
        .addAsWebInfResource(file("src/test/webapp/WEB-INF/jboss-deployment-structure.xml"))
        .addClass(ManagementModelSystemTest.class)
        .addClass(TestClasspath.class);
  }
}
