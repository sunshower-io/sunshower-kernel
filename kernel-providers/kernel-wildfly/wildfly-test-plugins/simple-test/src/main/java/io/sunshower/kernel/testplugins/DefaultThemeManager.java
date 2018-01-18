package io.sunshower.kernel.testplugins;

import io.sunshower.kernel.api.PluginManager;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class DefaultThemeManager implements ThemeManager {
    
    @Inject
    private PluginManager pluginManager;

    @Override
    public List<Theme> themes() {
        return null;
    }

    @Override
    public Theme getActiveTheme() {
        return null;
    }
    
    @PostConstruct
    public void postConstruct() {
        pluginManager.register(ThemeManager.class, this);
    }
}
