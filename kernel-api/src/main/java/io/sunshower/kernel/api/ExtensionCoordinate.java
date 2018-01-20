package io.sunshower.kernel.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class ExtensionCoordinate {
    
    private final String group;
    private final String namespace;
    private final String name;
    private final String version;
    
}
