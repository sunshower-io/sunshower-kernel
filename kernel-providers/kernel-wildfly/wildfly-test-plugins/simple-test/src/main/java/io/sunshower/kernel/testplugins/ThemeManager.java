package io.sunshower.kernel.testplugins;

import io.sunshower.kernel.api.ExtensionPoint;

import java.util.List;

@ExtensionPoint(group = "admin", namespace = "frapper", value = "theme")
public interface ThemeManager {

  void register(Theme theme);

  List<Theme> themes();

  Theme getActiveTheme();
}
