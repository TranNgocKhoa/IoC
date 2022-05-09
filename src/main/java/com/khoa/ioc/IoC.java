package com.khoa.ioc;

import com.khoa.ioc.annotation.*;
import com.khoa.ioc.exception.IoCException;
import com.khoa.ioc.loader.IoCClassLoader;

import java.lang.reflect.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class IoC {
    private final BeanContainer beanContainer = new BeanContainer();
    private final ImplementationContainer implementationContainer = new ImplementationContainer();

    /**
     * Scan and initiate beans
     *
     * @param mainClass
     * @param predefinedBeans
     * @return
     */
    public static IoC initBeans(Class<?> mainClass, Object... predefinedBeans) {
        IoC ioC = new IoC();
        ioC.doInitBeans(mainClass, predefinedBeans);

        return ioC;
    }

    private void doInitBeans(Class<?> mainClass, Object[] predefinedBeans) {
        // put predefined beans
        this.initPredefinedBeans(predefinedBeans);
        this.iniBeansFromMainClass(mainClass);
    }

    private void iniBeansFromMainClass(Class<?> mainClass) {
        ComponentScan scan = mainClass.getAnnotation(ComponentScan.class);
        if (scan != null) {
            String[] packages = scan.value();
            for (String packageName : packages) {
                this.initBeansInPackage(packageName);
            }
        }
    }

    private void initBeansInPackage(String packageName) {
        beanContainer.putBean(IoC.class, this);
        implementationContainer.putImplementationClass(IoC.class, IoC.class);

        // get classes in package name
        List<Class<?>> classesInPackage = IoCClassLoader.getInstance().getClassesInPackage(packageName);
        // init implementation container by @Component and @Bean in @Configuration classes
        this.scanAndRegisterImplementations(classesInPackage);
        // init beans in @Configuration classes
        this.initConfigurationDeclaredBeans(classesInPackage);
        // init beans from @Component classes
    }

    private void initConfigurationDeclaredBeans(List<Class<?>> classesInPackage) {
        Deque<Class<?>> configurationClassQueue = new ArrayDeque<>();
        classesInPackage.stream()
                .filter(aClass -> aClass.isAnnotationPresent(Configuration.class))
                .forEach(configurationClassQueue::add);

        while (!configurationClassQueue.isEmpty()) {
            // Create configuration object. Considered as a Component
            Class<?> configurationClass = configurationClassQueue.removeFirst();
            try {
                this.tryInitBeanConfigurationClass(configurationClass);
            } catch (Exception e) {
                configurationClassQueue.addLast(configurationClass);
            }
        }
    }

    private void tryInitBeanConfigurationClass(Class<?> configurationClass) throws IllegalAccessException, InvocationTargetException {
        _initBean(configurationClass, null, configurationClass.getSimpleName());

        // Init fields in Configuration object
        Object configurationObject = beanContainer.getBean(configurationClass, configurationClass.getSimpleName());
        List<Field> autowiredFields = Arrays.stream(configurationClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Autowired.class))
                .collect(Collectors.toList());

        for (Field autowiredField : autowiredFields) {
            String qualifier = autowiredField.isAnnotationPresent(Qualifier.class) ? autowiredField.getAnnotation(Qualifier.class).value() : null;

            _initBean(autowiredField.getType(), autowiredField.getAnnotation(Qualifier.class), autowiredField.getName());
            Object fieldInstance = beanContainer.getBean(autowiredField.getType(), qualifier != null ? qualifier : autowiredField.getName());
            autowiredField.set(configurationObject, fieldInstance);
        }

        List<Method> methods = Arrays.stream(configurationClass.getMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .collect(Collectors.toList());

        for (Method method : methods) {
            Class<?> beanType = method.getReturnType();
            Object beanInstance = method.invoke(configurationObject);
            String name = method.getAnnotation(Bean.class).value() != null ?
                    method.getAnnotation(Bean.class).value() : beanType.getName();
            beanContainer.putBean(beanType, beanInstance, name);
        }
    }

    // Init class type instance and put in BeanContainer
    private void _initBean(Class<?> type, Qualifier qualifier, String variableName) {
        String beanName = qualifier != null ? qualifier.value() : variableName;
        if (beanContainer.containsBean(type, beanName)) {
            return;
        }

        Class<?> beanType = implementationContainer.getImplementationClass(type, beanName);

        Constructor<?>[] constructors = beanType.getConstructors();

        if (constructors.length == 0) {
            throw new IoCException("There is no public constructor for class " + beanType + ". Invalid to init.");
        }

        if (constructors.length > 1) {
            throw new IoCException("There are " + constructors.length + " constructors for class " + beanType
                    + ". Can't define which one need to be chosen to be init.");
        }

        Object instance;
        try {
            Parameter[] parameters = constructors[0].getParameters();
            if (parameters.length == 0) {
                instance = beanType.getConstructor().newInstance();
            } else {
                for (Parameter parameter : parameters) {
                    Class<?> parameterType = parameter.getType();
                    Qualifier parameterAnnotation = parameter.getAnnotation(Qualifier.class);
                    String parameterName = parameter.getName();

                    _initBean(parameterType, parameterAnnotation, parameterName);
                }

                Class<?>[] parameterClasses = Arrays.stream(parameters)
                        .map(Parameter::getClass).toArray(Class<?>[]::new);

                Object[] parameterObjects = Arrays.stream(parameterClasses)
                        .map(this::getBean)
                        .toArray(Object[]::new);

                instance = beanType.getConstructor(parameterClasses).newInstance(parameterObjects);
            }
        } catch (Exception e) {
            throw new IoCException("Error when init bean with type = " + beanType + ", qualifier = " + qualifier + ", name = " + variableName);
        }

        beanContainer.putBean(beanType, instance, beanName);
    }

    private void scanAndRegisterImplementations(List<Class<?>> classesInPackage) {
        List<Class<?>> componentClasses = classesInPackage.stream()
                .filter(aClass -> aClass.isAnnotationPresent(Component.class))
                .collect(Collectors.toList());
        this.registerImplementationsForComponent(componentClasses);

        List<Class<?>> configurationClasses = classesInPackage.stream()
                .filter(aClass -> aClass.isAnnotationPresent(Configuration.class))
                .collect(Collectors.toList());
        this.registerImplementationForBeansInConfigurationClasses(configurationClasses);
    }

    private void registerImplementationForBeansInConfigurationClasses(List<Class<?>> configurationClasses) {
        for (Class<?> configurationClass : configurationClasses) {
            List<Method> beanMethods = Arrays.stream(configurationClass.getMethods())
                    .filter(method -> method.getAnnotation(Bean.class) != null)
                    .collect(Collectors.toList());

            for (Method method : beanMethods) {
                Class<?> returnType = method.getReturnType();
                implementationContainer.putImplementationClass(returnType, returnType);
            }
        }
    }

    private void registerImplementationsForComponent(List<Class<?>> componentClasses) {
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

    private void initPredefinedBeans(Object[] predefinedBeans) {
        for (Object predefinedBean : predefinedBeans) {
            Class<?>[] interfaces = predefinedBean.getClass().getInterfaces();

            if (interfaces.length == 0) {
                this.implementationContainer.putImplementationClass(predefinedBean.getClass(), predefinedBean.getClass());
            } else {
                for (Class<?> anInterface : interfaces) {
                    this.implementationContainer.putImplementationClass(anInterface, predefinedBean.getClass());
                }
            }

            beanContainer.putBean(predefinedBean.getClass(), predefinedBean);
        }
    }

    /**
     * Get bean by Class
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getBean(Class<T> clazz) {
        return null;
    }


}
