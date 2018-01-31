package io.sunshower.kernel.api;


public class ServletExtensionMetadata implements ExtensionMetadata {

    private final String contextPath;

    public ServletExtensionMetadata(String contextPath) {
        this.contextPath = contextPath;
    }


    @Override
    public String getDeploymentLocation() {
        return contextPath;
    }
}
