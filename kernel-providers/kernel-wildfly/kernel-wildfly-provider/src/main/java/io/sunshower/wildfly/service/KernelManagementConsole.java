package io.sunshower.wildfly.service;

import io.sunshower.wildfly.model.ManagementRequestElement;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("management")
public interface KernelManagementConsole {

  @GET
  @Path("/")
  public ManagementRequestElement getKernelDescription();
}
