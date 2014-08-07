package com.github.rmannibucau.annotations.configuration.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlType(name = "class")
public class MetaClass extends MetaAnnotated {
    private static final MetaMethod EMPTY_METHOD = new MetaMethod();
    private static final MetaField EMPTY_FIELD = new MetaField();

    @XmlElement(name = "method")
    private List<MetaMethod> methods = new ArrayList<>(8);

    @XmlElement(name = "field")
    private List<MetaField> fields = new ArrayList<>(8);

    public List<MetaMethod> getMethods() {
        return methods;
    }

    public List<MetaField> getFields() {
        return fields;
    }

    public MetaMethod getMethod(final String name) {
        return findByName(name, methods, EMPTY_METHOD);
    }

    public MetaField getField(final String name) {
        return findByName(name, fields, EMPTY_FIELD);
    }
}
