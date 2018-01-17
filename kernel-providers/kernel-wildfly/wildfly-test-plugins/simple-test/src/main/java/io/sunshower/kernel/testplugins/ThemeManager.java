package io.sunshower.kernel.testplugins;


import io.sunshower.kernel.api.ExtensionPoint;

import java.util.List;

@ExtensionPoint
public interface ThemeManager {
    
    List<Theme> themes();
    
    Theme getActiveTheme();
    
    
}
