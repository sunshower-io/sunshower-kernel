package io.sunshower.kernel;

public interface InjectionContext {
    
    void register(Class<?> type, Object value);
    
    void inject(Class<?> type, Object instance);
    
}
