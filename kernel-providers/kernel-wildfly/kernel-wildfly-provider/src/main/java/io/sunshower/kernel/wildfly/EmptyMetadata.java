package io.sunshower.kernel.wildfly;

import io.sunshower.kernel.api.ExtensionMetadata;

public class EmptyMetadata implements ExtensionMetadata {
    @Override
    public String getDeploymentLocation() {
        return EmptyMetadata.class.getProtectionDomain().getCodeSource().getLocation().toExternalForm();
    }
}
