package com.github.rmannibucau.annotations.configuration.xml;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

@XmlType(name = "model")
@XmlAccessorType(FIELD)
public class MetaModel {
    private static final MetaClass EMPTY_CLASS = new MetaClass();

    @XmlElement(name = "class")
    private List<MetaClass> classes = new ArrayList<>(2);

    public List<MetaClass> getClasses() {
        return classes;
    }

    public MetaClass getMeta(final String name) {
        return MetaAnnotated.findByName(name, classes, EMPTY_CLASS);
    }
}
