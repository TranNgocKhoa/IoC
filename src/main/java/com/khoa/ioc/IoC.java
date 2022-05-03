package com.khoa.ioc;

import com.khoa.ioc.annotation.ComponentScan;
import com.khoa.ioc.loader.IoCClassLoader;

import java.util.List;

public class IoC {
    private final BeanContainer beanContainer = new BeanContainer();
    private final ImplementationContainer implementationContainer = new ImplementationContainer();

    /**
     * Scan and initiate beans
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
        // init beans in @Configuration classes
        // init beans from @Component classes
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
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getBean(Class<T> clazz) {
        return null;
    }


}
