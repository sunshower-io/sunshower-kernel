package io.sunshower.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import io.sunshower.test.common.TestClasspath;
import io.sunshower.wildfly.features.HttpAuthenticationFeature;
import io.sunshower.wildfly.model.DeploymentDescriptorElement;
import io.sunshower.wildfly.service.KernelManagementConsole;
import java.io.File;
import lombok.val;
import org.eclipse.persistence.jaxb.rs.MOXyJsonProvider;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ManagementModelSystemTest {

  private KernelManagementConsole managementConsole;

  @Deployment
  public static WebArchive deployment() {
    return Deployments.baseDeployment();
  }

  static File file(String path) {
    return new File(TestClasspath.buildDir().getParentFile(), path);
  }

  @Before
  public void setUp() {

    val feature = HttpAuthenticationFeature.digest("sunshower-kernel", "sunshower-kernel");
    val client =
        (ResteasyClient)
            ResteasyClientBuilder.newBuilder()
                .register(feature)
                .register(MOXyJsonProvider.class)
                .build();
    managementConsole = client.target("http://localhost:9990").proxy(KernelManagementConsole.class);
  }

  @Test
  public void ensureCorrectNumberOfPluginsAreDeployed() {
    val deployments = managementConsole.getKernelDescription().getDeployments();
    assertThat(deployments.size(), is(4));
  }

  @Test
  public void ensureCurrentPluginIsDeployed() {
    managementConsole.getKernelDescription().getDeployments().forEach(System.out::println);
    val cplugin =
        managementConsole
            .getKernelDescription()
            .getDeployments()
            .stream()
            .filter(t -> t.getName().equals("kernel-test"))
            .findAny();
    assertThat(cplugin.isPresent(), is(true));
    cplugin
        .map(DeploymentDescriptorElement::getName)
        .stream()
        .forEach(t -> assertThat(t, is("kernel-test")));
  }
}
