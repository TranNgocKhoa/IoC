package com.khoa.ioc.loader;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IoCClassLoaderTest {

    @Test
    void getClassesInPackage() {
        List<Class<?>> classesInPackage = IoCClassLoader.getInstance()
                .getClassesInPackage("com.khoa.ioc");
    }
}