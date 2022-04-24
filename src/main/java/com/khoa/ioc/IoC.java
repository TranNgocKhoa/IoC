package com.khoa.ioc;

public class IoC {

    private IoC() {
    }

    /**
     * Scan and initiate beans
     * @param mainClass
     * @param predefinedBeans
     * @return
     */
    public static IoC initBeans(Class<?> mainClass, Object... predefinedBeans) {
        // call
        return null;
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
