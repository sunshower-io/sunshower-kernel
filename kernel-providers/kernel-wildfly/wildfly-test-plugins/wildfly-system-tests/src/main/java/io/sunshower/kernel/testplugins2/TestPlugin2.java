package io.sunshower.kernel.testplugins2;

import io.sunshower.kernel.testplugins.Theme;
import io.sunshower.kernel.testplugins.ThemeManager;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
public class TestPlugin2 {


    @Resource(name = "java:global/simple-test-1.0.0-SNAPSHOT/DefaultThemeManager!io.sunshower.kernel.testplugins.ThemeManager")
    private ThemeManager themeManager;
    
    @PostConstruct
    public void postConstruct() {
        this.themeManager.register(new Theme() {});
    }

    public ThemeManager getThemeManager() {
        return themeManager;
    }

    public void setThemeManager(ThemeManager themeManager) {
        this.themeManager = themeManager;
    }
}
