package com.github.rmannibucau.annotations.configuration;

import com.github.rmannibucau.annotations.configuration.xml.MetaAnnotated;
import com.github.rmannibucau.annotations.configuration.xml.MetaAnnotation;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public abstract class Configurable<T, M extends MetaAnnotated> {
    private final T reflectInstance;
    private final M meta;
    private final Map<Class<?>, Annotation> annotations = new HashMap<>();

    public Configurable(final T reflectInstance, final M meta) {
        this.reflectInstance = reflectInstance;
        this.meta = meta;

        initAnnotations(annotations(reflectInstance), meta.annotations());
        for (final MetaAnnotation m : meta.getAnnotations()) {
            if (m.isRemove()) {
                try {
                    annotations.remove(Thread.currentThread().getContextClassLoader().loadClass(m.getClazz()));
                } catch (final ClassNotFoundException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }
    }

    protected abstract Annotation[] annotations(T reflectInstance);

    private void initAnnotations(final Annotation[]... clazzAnnotations) {
        for (final Annotation[] as : clazzAnnotations) {
            for (final Annotation a : as) {
                annotations.put(a.annotationType(), a);
            }
        }
    }

    public T getReflectInstance() {
        return reflectInstance;
    }

    public M getMeta() {
        return meta;
    }

    public <A extends Annotation> A getAnnotation(final Class<A> clazz) {
        return clazz.cast(annotations.get(clazz));
    }

    public Map<Class<?>, Annotation> getAnnotations() {
        return annotations;
    }
}
