package com.github.rmannibucau.annotations.configuration;

import com.github.rmannibucau.annotations.configuration.xml.MetaField;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class ConfigurableField extends Configurable<Field, MetaField> {
    public ConfigurableField(final Field field, final MetaField meta) {
        super(field, meta);
    }

    @Override
    protected Annotation[] annotations(final Field reflectInstance) {
        return reflectInstance.getAnnotations();
    }
}
