package com.github.rmannibucau.annotations.configuration;

import com.github.rmannibucau.annotations.configuration.xml.MetaMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ConfigurableMethod extends Configurable<Method, MetaMethod> {
    public ConfigurableMethod(final Method method, final MetaMethod meta) {
        super(method, meta);
    }

    @Override
    protected Annotation[] annotations(final Method reflectInstance) {
        return reflectInstance.getAnnotations();
    }
}
