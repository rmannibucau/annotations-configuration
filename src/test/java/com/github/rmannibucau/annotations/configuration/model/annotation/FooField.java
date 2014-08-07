package com.github.rmannibucau.annotations.configuration.model.annotation;

import com.github.rmannibucau.annotations.configuration.model.AClass;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
public @interface FooField {
    String value();
    int bar() default 1;
    Class<?> provider() default AClass.class;
}
