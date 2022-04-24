package com.khoa.ioc;

import java.util.HashMap;
import java.util.Map;

public class BeanContainer {
    private final Map<Class<?>, Map<String, Object>> beans = new HashMap<>();

    public void putBean(Class<?> clazz, Object instance) {

    }

    public <T> T getBean(Class<T> clazz) {
        return null;
    }

    public boolean containsBean(Class<?> clazz) {
        return false;
    }

    public boolean containsBean(Class<?> clazz, String name) {
        return false;
    }
}
