package com.khoa.ioc.loader;

import com.khoa.ioc.exception.IoCClassLoaderException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class IoCClassLoader {
    private static IoCClassLoader INSTANCE;

    public static IoCClassLoader getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }

        synchronized (IoCClassLoader.class) {
            if (INSTANCE != null) {
                return INSTANCE;
            }
            INSTANCE = new IoCClassLoader();

            return INSTANCE;
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
        List<Class<?>> classesInPackage = new ArrayList<>();

        classReader.lines()
                .forEach(fileName -> this.processSubFile(packageName, classesInPackage, fileName));

        return classesInPackage;
    }

    private void processSubFile(String packageName, List<Class<?>> classesInPackage, String fileName) {
        if (fileName.endsWith(".class")) {
            classesInPackage.add(getClass(fileName, packageName));
        } else {
            List<Class<?>> classesInSubPackage = this.getClassesInPackage(packageName + "." + fileName);

            if (!classesInSubPackage.isEmpty()) {
                classesInPackage.addAll(classesInSubPackage);
            }
        }
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
