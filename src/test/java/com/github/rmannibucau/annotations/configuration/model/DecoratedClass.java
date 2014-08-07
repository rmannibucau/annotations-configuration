package com.github.rmannibucau.annotations.configuration.model;

import com.github.rmannibucau.annotations.configuration.model.annotation.FooField;
import com.github.rmannibucau.annotations.configuration.model.annotation.FooMethod;
import com.github.rmannibucau.annotations.configuration.model.annotation.FooType;

@FooType("foo")
public class DecoratedClass {
    @FooField(value = "yeah", bar = 5, provider = DecoratedClass.class)
    private String aField;

    @FooMethod(value = "yeah", bar = 5, provider = DecoratedClass.class)
    public String aMethod(final String w) {
        return null;
    }
}
