package com.khoa.ioc;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains mapping between Interface and its implementation(s). Because normally each interface should have one or many
 * implementations, the map should be Map<ImplementationClass, InterfaceClass> map.
 */
public class ImplementationContainer {
    private final Map<Class<?>, Class<?>> implementationsInterfaceMap = new HashMap<>();

    public void putImplementationClass(Class<?> implementationClass, Class<?> interfaceClass) {

    }

    /**
     * Return Implementation class from Interface class, autowired Field Name, qualifier
     * @param interfaceClass
     * @param autowiredFieldName
     * @param qualifier
     * @return
     */
    public Class<?> getImplementationClass(Class<?> interfaceClass, String autowiredFieldName, String qualifier) {
        return null;
    }
}
