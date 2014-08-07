package com.github.rmannibucau.annotations.configuration.xml;

import org.apache.xbean.propertyeditor.PropertyEditors;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

@XmlAccessorType(FIELD)
public abstract class MetaAnnotated {
    @XmlAttribute
    private String name;

    @XmlElement(name = "annotation")
    private List<MetaAnnotation> annotations = new ArrayList<>(2);

    @XmlTransient
    private Annotation[] cache;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<MetaAnnotation> getAnnotations() {
        return annotations;
    }

    public Annotation[] annotations() {
        if (cache != null) {
            return cache;
        }

        final Collection<Annotation> list = new ArrayList<>(annotations.size());
        for (final MetaAnnotation annotation : annotations) {
            if (annotation.isRemove()) {
                continue;
            }
            list.add(buildAnnotation(annotation));
        }
        cache = list.toArray(new Annotation[list.size()]);
        return cache;
    }

    private Annotation buildAnnotation(final MetaAnnotation annotation) {
        final String annotationClazz = annotation.getClazz();
        if (annotationClazz == null) {
            throw new IllegalArgumentException("no annotation class for " + getName());
        }

        final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            final Class<?> api = contextClassLoader.loadClass(annotationClazz);
            return Annotation.class.cast(
                    Proxy.newProxyInstance(
                            contextClassLoader,
                            new Class<?>[] { api },
                            new AnnotationHandler(api, annotation)
                    )
            );
        } catch (final ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T extends MetaAnnotated> T findByName(final String name, final Collection<T> annotated, final T defaultInstance) {
        for (final T t : annotated) {
            if (name.equals(t.getName())) {
                return t;
            }
        }
        return defaultInstance;
    }

    private static class AnnotationHandler implements InvocationHandler {
        private final Map<String, Object> members;
        private final Class<?> api;

        private AnnotationHandler(final Class<?> api, final MetaAnnotation annotation) {
            this.api = api;
            this.members = new HashMap<>();
            for (final MetaAttribute attribute : annotation.getAttributes()) {
                final String attributeName = attribute.getName();
                final String value = attribute.getValue();
                try {
                    final Method member = api.getMethod(attributeName);
                    this.members.put(attributeName, value == null ? member.getDefaultValue() : coerce(value, member.getReturnType()));
                } catch (final NoSuchMethodException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }

        private Object coerce(final String value, final Class<?> returnType) {
            return PropertyEditors.getValue(returnType, value);
        }

        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
            if (Object.class.equals(method.getDeclaringClass())) { // to make debug simpler
                return method.invoke(this, args);
            }
            if (Annotation.class == method.getDeclaringClass()) {
                if (method.getName().equals("annotationType")) {
                    return api;
                }
                return method.invoke(this, args);
            }
            return members.get(method.getName());
        }
    }
}
