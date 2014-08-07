package com.github.rmannibucau.annotations.configuration.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class DecoratedClassWithOther {
    @XmlElement
    private String aField;

    @XmlElement
    public String aMethod(final String w) {
        return null;
    }
}
