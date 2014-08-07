package com.github.rmannibucau.annotations.configuration;

import com.github.rmannibucau.annotations.configuration.xml.MetaClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static java.util.Arrays.asList;

public class ConfigurableClass extends Configurable<Class<?>, MetaClass> {
    public ConfigurableClass(final Class<?> reflectInstance, final MetaClass meta) {
        super(reflectInstance, meta);
    }

    @Override
    protected Annotation[] annotations(final Class<?> reflectInstance) {
        return reflectInstance.getAnnotations();
    }

    // TODO: support parameters
    public ConfigurableMethod getMethod(final String name, final Class<?>... parameters) {
        return new ConfigurableMethod(findMethod(name, parameters), getMeta().getMethod(name));
    }

    public ConfigurableField getField(final String name) {
        return new ConfigurableField(findField(name), getMeta().getField(name));
    }

    public Collection<ConfigurableField> fields() {
        final Collection<Field> f;
        Class<?> current = getReflectInstance();
        if (current.isInterface()) {
            return Collections.emptyList();
        } else {
            f = new ArrayList<>(8);
            while (current != Object.class) { // skip Object since there is no annotations so it can be "cached" by user
                f.addAll(asList(current.getDeclaredFields()));
                current = current.getSuperclass();
            }
        }

        final Collection<ConfigurableField> cf = new ArrayList<>(f.size());
        for (final Field field : f) {
            cf.add(getField(field.getName()));
        }
        return cf;
    }

    // TODO: handle inheritance/overriding
    public Collection<ConfigurableMethod> methods() {
        final Collection<Method> m;
        Class<?> current = getReflectInstance();
        if (current.isInterface()) {
            m = asList(current.getMethods());
        } else {
            m = new ArrayList<>(8);
            while (current != Object.class) { // skip Object since there is no annotations so it can be "cached" by user
                m.addAll(asList(current.getDeclaredMethods()));
                current = current.getSuperclass();
            }
        }

        final Collection<ConfigurableMethod> cm = new ArrayList<>(m.size());
        for (final Method mtd : m) {
            cm.add(getMethod(mtd.getName(), mtd.getParameterTypes()));
        }
        return cm;
    }

    private Method findMethod(final String name, final Class<?>[] parameters) {
        Class<?> current = getReflectInstance();
        if (current.isInterface()) {
            try {
                return getReflectInstance().getMethod(name, parameters);
            } catch (final NoSuchMethodException e) {
                throw new IllegalStateException("Method " + name + " not found");
            }
        }

        while (current != null) {
            try {
                return getReflectInstance().getDeclaredMethod(name, parameters);
            } catch (final NoSuchMethodException e) {
                // no-op
            }
            current = current.getSuperclass();
        }
        throw new IllegalStateException("Method " + name + " not found in " + getReflectInstance().getName());
    }

    private Field findField(final String name) {
        Class<?> current = getReflectInstance();
        if (current.isInterface()) {
            throw new IllegalStateException("No field on interfaces");
        }

        while (current != null) {
            try {
                return getReflectInstance().getDeclaredField(name);
            } catch (final NoSuchFieldException e) {
                // no-op
            }
            current = current.getSuperclass();
        }
        throw new IllegalStateException("Field " + name + " not found in " + getReflectInstance().getName());
    }
}
