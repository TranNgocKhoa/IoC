package com.khoa.ioc.loader;

import com.khoa.ioc.exception.IoCClassLoaderException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class IoCClassLoader {
    private static IoCClassLoader INSTANCE;

    public static IoCClassLoader getInstance() {
        if (INSTANCE != null) {
            return getInstance();
        }

        synchronized (IoCClassLoader.class) {
            if (INSTANCE != null) {
                return INSTANCE;
            }

            return new IoCClassLoader();
        }
    }

    private IoCClassLoader() {
    }

    public List<Class<?>> getClassesInPackage(String packageName) {
        InputStream classStream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));

        if (classStream == null) {
            throw new IoCClassLoaderException("Can't retrieve class InputStream from package " + packageName);
        }

        BufferedReader classReader = new BufferedReader(new InputStreamReader(classStream));

        return classReader.lines()
                .filter(className -> className.endsWith(".class"))
                .map(className -> getClass(className, packageName))
                .collect(Collectors.toList());
    }

    private Class<?> getClass(String className, String packageName) {
        String classFullName = packageName + "." + className.substring(0, className.lastIndexOf('.'));
        try {
            return Class.forName(classFullName);
        } catch (ClassNotFoundException e) {
            throw new IoCClassLoaderException("Can't retrieve class for name " + classFullName);
        }
    }
}
