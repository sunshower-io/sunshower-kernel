package io.sunshower.kernel.wildfly.service;

import io.sunshower.kernel.wildfly.model.ManagementRequestElement;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("management")
public interface KernelManagementConsole {

  @GET
  @Path("/")
  public ManagementRequestElement getKernelDescription();
}
