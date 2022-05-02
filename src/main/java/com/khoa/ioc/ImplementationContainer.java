package com.khoa.ioc;

import com.khoa.ioc.exception.IoCException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Contains mapping between Interface and its implementation(s). Because normally each interface should have one or many
 * implementations, the map should be Map&lt;InterfaceClass, Set&lt;InterfaceClass&gt;&gt; map.
 */
public class ImplementationContainer {
    private final Map<Class<?>, Set<Class<?>>> interfaceImplementationsMap = new HashMap<>();

    public void putImplementationClass(Class<?> interfaceClass, Class<?> implementationClass) {
        Set<Class<?>> interfaceClassList = interfaceImplementationsMap.computeIfAbsent(interfaceClass, k -> new HashSet<>());
        interfaceClassList.add(implementationClass);
    }

    /**
     * Return Implementation class from Interface class, autowired Field Name, qualifier
     *
     * @param interfaceClass
     * @param autowiredFieldName
     * @param qualifier
     * @return
     */
    public Class<?> getImplementationClass(Class<?> interfaceClass, String autowiredFieldName, String qualifier) {
        Set<Class<?>> implementationClassSet = interfaceImplementationsMap.get(interfaceClass);

        if (implementationClassSet == null || implementationClassSet.isEmpty()) {
            throw new IoCException("No implementation found for interface " + interfaceClass.getName());
        }

        if (implementationClassSet.size() == 1) {
            return implementationClassSet.iterator().next();
        }

        String searchFactor = (qualifier == null || qualifier.trim().length() == 0) ? autowiredFieldName : qualifier;

        return implementationClassSet.stream()
                .filter(clazz -> clazz.getSimpleName().equals(searchFactor))
                .findFirst()
                .orElseThrow(() -> new IoCException("There are " + implementationClassSet.size()
                        + " of interface " + interfaceClass.getName()
                        + " Expected single implementation or make use of"
                        + " @Qualifier to resolve conflict"));
    }
}
