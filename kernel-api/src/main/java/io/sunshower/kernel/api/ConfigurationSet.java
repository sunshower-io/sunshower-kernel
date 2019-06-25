package io.sunshower.kernel.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfigurationSet {
    private String category;
    private Class<?> configurableType;
    private Class<?> configurationType;
}
