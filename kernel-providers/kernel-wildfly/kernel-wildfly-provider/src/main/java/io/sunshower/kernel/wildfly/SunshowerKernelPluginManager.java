package io.sunshower.kernel.wildfly;

import javax.ejb.EJB;
import javax.ejb.Singleton;

@Singleton
@EJB(name = "java:global/sunshower/kernel/plugin-manager")
public class SunshowerKernelPluginManager {}
