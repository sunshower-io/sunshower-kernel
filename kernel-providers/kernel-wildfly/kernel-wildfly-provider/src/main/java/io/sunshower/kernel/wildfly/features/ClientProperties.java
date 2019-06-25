package io.sunshower.kernel.wildfly.features;

import java.security.PrivilegedAction;
import java.util.Map;
import java.util.Properties;
import javax.ws.rs.RuntimeType;

public final class ClientProperties {

  /**
   * Get system properties.
   *
   * <p>This method delegates to {@link System#getProperties()} while running it in a privileged
   * code block.
   *
   * @return privileged action to obtain system properties.
   */
  public static PrivilegedAction<Properties> getSystemProperties() {
    return () -> System.getProperties();
  }

  /**
   * Get system property.
   *
   * <p>This method delegates to {@link System#getProperty(String)} while running it in a privileged
   * code block.
   *
   * @param name system property name.
   * @return privileged action to obtain system property value that will return {@code null} if
   *     there's no such system property.
   */
  public static PrivilegedAction<String> getSystemProperty(final String name) {
    return new PrivilegedAction<String>() {
      @Override
      public String run() {
        return System.getProperty(name);
      }
    };
  }

  /**
   * Get system property.
   *
   * <p>This method delegates to {@link System#getProperty(String)} while running it in a privileged
   * code block.
   *
   * @param name system property name.
   * @param def default property value.
   * @return privileged action to obtain system property value that will return the default value if
   *     there's no such system property.
   */
  public static PrivilegedAction<String> getSystemProperty(final String name, final String def) {
    return new PrivilegedAction<String>() {
      @Override
      public String run() {
        return System.getProperty(name, def);
      }
    };
  }

  /**
   * Return value of a specified property. If the property is not set or the real value type is not
   * compatible with defaultValue type, the specified defaultValue is returned. Calling this method
   * is equivalent to calling {@code PropertyHelper.getValue(properties, key, defaultValue,
   * (Class<T>) defaultValue.getClass())}
   *
   * @param properties Map of properties to get the property value from.
   * @param key Name of the property.
   * @param defaultValue Default value to be returned if the specified property is not set or cannot
   *     be read.
   * @param <T> Type of the property value.
   * @param legacyMap Legacy fallback map, where key is the actual property name, value is the old
   *     property name
   * @return Value of the property or defaultValue.
   */
  public static <T> T getValue(
      Map<String, ?> properties, String key, T defaultValue, Map<String, String> legacyMap) {
    return getValue(properties, null, key, defaultValue, legacyMap);
  }

  /**
   * Return value of a specified property. If the property is not set or the real value type is not
   * compatible with defaultValue type, the specified defaultValue is returned. Calling this method
   * is equivalent to calling {@code PropertyHelper.getValue(properties, runtimeType, key,
   * defaultValue, (Class<T>) defaultValue.getClass())}
   *
   * @param properties Map of properties to get the property value from.
   * @param runtimeType Runtime type which is used to check whether there is a property with the
   *     same {@code key} but post-fixed by runtime type (<tt>.server</tt> or {@code .client}) which
   *     would override the {@code key} property.
   * @param key Name of the property.
   * @param defaultValue Default value to be returned if the specified property is not set or cannot
   *     be read.
   * @param <T> Type of the property value.
   * @param legacyMap Legacy fallback map, where key is the actual property name, value is the old
   *     property name
   * @return Value of the property or defaultValue.
   */
  @SuppressWarnings("unchecked")
  public static <T> T getValue(
      Map<String, ?> properties,
      RuntimeType runtimeType,
      String key,
      T defaultValue,
      Map<String, String> legacyMap) {
    return getValue(
        properties, runtimeType, key, defaultValue, (Class<T>) defaultValue.getClass(), legacyMap);
  }

  /**
   * Returns value of a specified property. If the property is not set or the real value type is not
   * compatible with the specified value type, returns defaultValue.
   *
   * @param properties Map of properties to get the property value from.
   * @param key Name of the property.
   * @param defaultValue Default value of the property.
   * @param type Type to retrieve the value as.
   * @param <T> Type of the property value.
   * @param legacyMap Legacy fallback map, where key is the actual property name, value is the old
   *     property name
   * @return Value of the property or null.
   */
  public static <T> T getValue(
      Map<String, ?> properties,
      String key,
      T defaultValue,
      Class<T> type,
      Map<String, String> legacyMap) {
    return getValue(properties, null, key, defaultValue, type, legacyMap);
  }

  /**
   * Returns value of a specified property. If the property is not set or the real value type is not
   * compatible with the specified value type, returns defaultValue.
   *
   * @param properties Map of properties to get the property value from.
   * @param runtimeType Runtime type which is used to check whether there is a property with the
   *     same {@code key} but post-fixed by runtime type (<tt>.server</tt> or {@code .client}) which
   *     would override the {@code key} property.
   * @param key Name of the property.
   * @param defaultValue Default value of the property.
   * @param type Type to retrieve the value as.
   * @param <T> Type of the property value.
   * @param legacyMap Legacy fallback map, where key is the actual property name, value is the old
   *     property name
   * @return Value of the property or null.
   */
  public static <T> T getValue(
      Map<String, ?> properties,
      RuntimeType runtimeType,
      String key,
      T defaultValue,
      Class<T> type,
      Map<String, String> legacyMap) {
    T value = getValue(properties, runtimeType, key, type, legacyMap);
    if (value == null) {
      value = defaultValue;
    }
    return value;
  }

  /**
   * Returns value of a specified property. If the property is not set or the real value type is not
   * compatible with the specified value type, returns null.
   *
   * @param properties Map of properties to get the property value from.
   * @param key Name of the property.
   * @param type Type to retrieve the value as.
   * @param <T> Type of the property value.
   * @param legacyMap Legacy fallback map, where key is the actual property name, value is the old
   *     property name
   * @return Value of the property or null.
   */
  public static <T> T getValue(
      Map<String, ?> properties, String key, Class<T> type, Map<String, String> legacyMap) {
    return getValue(properties, null, key, type, legacyMap);
  }

  /**
   * Returns value of a specified property. If the property is not set or the real value type is not
   * compatible with the specified value type, returns null.
   *
   * @param properties Map of properties to get the property value from.
   * @param runtimeType Runtime type which is used to check whether there is a property with the
   *     same {@code key} but post-fixed by runtime type (<tt>.server</tt> or {@code .client}) which
   *     would override the {@code key} property.
   * @param key Name of the property.
   * @param type Type to retrieve the value as.
   * @param <T> Type of the property value.
   * @return Value of the property or null.
   */
  public static <T> T getValue(
      Map<String, ?> properties,
      RuntimeType runtimeType,
      String key,
      Class<T> type,
      Map<String, String> legacyMap) {
    Object value = null;
    if (runtimeType != null) {
      String runtimeAwareKey = getPropertyNameForRuntime(key, runtimeType);
      if (key.equals(runtimeAwareKey)) {
        // legacy behaviour
        runtimeAwareKey = key + "." + runtimeType.name().toLowerCase();
      }
      value = properties.get(runtimeAwareKey);
    }
    if (value == null) {
      value = properties.get(key);
    }
    if (value == null) {
      value = getLegacyFallbackValue(properties, legacyMap, key);
    }
    if (value == null) {
      return null;
    }

    return (T) value;
  }

  /**
   * Returns specific property value for given {@link RuntimeType}.
   *
   * <p>Some properties have complementary client and server versions along with a common version
   * (effective for both environments, if the specific one is not set). This methods returns a
   * specific name for the environment (determined by convention), if runtime is not null, the
   * property is a Jersey property name (starts with {@code jersey.config}) and does not contain a
   * runtime specific part already. If those conditions are not met, original property name is
   * returned.
   *
   * @param key property name
   * @param runtimeType runtime type
   * @return runtime-specific property name, where possible, original key in other cases. original
   *     key
   */
  public static String getPropertyNameForRuntime(String key, RuntimeType runtimeType) {
    if (runtimeType != null && key.startsWith("jersey.config")) {
      RuntimeType[] types = RuntimeType.values();
      for (RuntimeType type : types) {
        if (key.startsWith("jersey.config." + type.name().toLowerCase())) {
          return key;
        }
      }
      return key.replace("jersey.config", "jersey.config." + runtimeType.name().toLowerCase());
    }
    return key;
  }

  private static Object getLegacyFallbackValue(
      Map<String, ?> properties, Map<String, String> legacyFallbackMap, String key) {
    if (legacyFallbackMap == null || !legacyFallbackMap.containsKey(key)) {
      return null;
    }
    String fallbackKey = legacyFallbackMap.get(key);
    Object value = properties.get(fallbackKey);
    return value;
  }

  /**
   * Get the value of the property with a given name converted to {@code boolean}. Returns {@code
   * false} if the value is not convertible.
   *
   * @param properties key-value map of properties.
   * @param name property name.
   * @return {@code boolean} property value or {@code false} if the property is not convertible.
   */
  public static boolean isProperty(final Map<String, Object> properties, final String name) {
    return properties.containsKey(name) && isProperty(properties.get(name));
  }

  /**
   * Get the value of the property converted to {@code boolean}. Returns {@code false} if the value
   * is not convertible.
   *
   * @param value property value.
   * @return {@code boolean} property value or {@code false} if the property is not convertible.
   */
  public static boolean isProperty(final Object value) {
    if (value instanceof Boolean) {
      return Boolean.class.cast(value);
    } else {
      return value != null && Boolean.parseBoolean(value.toString());
    }
  }
}
