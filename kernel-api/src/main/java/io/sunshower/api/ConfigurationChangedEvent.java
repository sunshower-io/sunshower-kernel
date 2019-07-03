package io.sunshower.api;

import lombok.Data;

@Data
public class ConfigurationChangedEvent {
  final Object configurationValue;
  final Class<?> configurationType;
}
