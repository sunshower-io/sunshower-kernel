package io.sunshower.kernel.api;

import lombok.*;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class ExtensionCoordinate {
    
    private final String group;
    private final String namespace;
    private final String name;
    private final String version;
    
}
