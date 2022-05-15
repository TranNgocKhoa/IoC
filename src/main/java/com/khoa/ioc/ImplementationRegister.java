package com.khoa.ioc;

import com.khoa.ioc.annotation.Bean;
import com.khoa.ioc.annotation.Component;
import com.khoa.ioc.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ImplementationRegister {
    private static ImplementationRegister INSTANCE;

    public static ImplementationRegister getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }

        synchronized (ImplementationRegister.class) {
            if (INSTANCE != null) {
                return INSTANCE;
            }
            INSTANCE = new ImplementationRegister();

            return INSTANCE;
        }
    }

    private ImplementationRegister() {
    }

    public void register(List<Class<?>> classesInPackage, ImplementationContainer implementationContainer) {
        List<Class<?>> componentClasses = classesInPackage.stream()
                .filter(aClass -> aClass.isAnnotationPresent(Component.class))
                .collect(Collectors.toList());
        this.registerImplementationsForComponent(componentClasses, implementationContainer);

        List<Class<?>> configurationClasses = classesInPackage.stream()
                .filter(aClass -> aClass.isAnnotationPresent(Configuration.class))
                .collect(Collectors.toList());
        this.registerImplementationForBeansInConfigurationClasses(configurationClasses, implementationContainer);
    }

    private void registerImplementationForBeansInConfigurationClasses(List<Class<?>> configurationClasses, ImplementationContainer implementationContainer) {
        for (Class<?> configurationClass : configurationClasses) {
            implementationContainer.putImplementationClass(configurationClass, configurationClass);
            List<Method> beanMethods = Arrays.stream(configurationClass.getMethods())
                    .filter(method -> method.getAnnotation(Bean.class) != null)
                    .collect(Collectors.toList());

            for (Method method : beanMethods) {
                Class<?> returnType = method.getReturnType();
                implementationContainer.putImplementationClass(returnType, returnType);
            }
        }
    }

    private void registerImplementationsForComponent(List<Class<?>> componentClasses, ImplementationContainer implementationContainer) {
        for (Class<?> implementationClass : componentClasses) {
            Class<?>[] interfaces = implementationClass.getInterfaces();
            if (interfaces.length == 0) {
                implementationContainer.putImplementationClass(implementationClass, implementationClass);
            } else {
                for (Class<?> interfaceClass : interfaces) {
                    implementationContainer.putImplementationClass(interfaceClass, implementationClass);
                }
            }
        }
    }
}
