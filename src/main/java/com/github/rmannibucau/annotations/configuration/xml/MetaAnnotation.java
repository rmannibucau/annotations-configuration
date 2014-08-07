package com.github.rmannibucau.annotations.configuration.xml;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

@XmlAccessorType(FIELD)
@XmlType(name = "annotation")
public class MetaAnnotation {
    @XmlAttribute(name = "class")
    private String clazz;

    @XmlAttribute
    private boolean remove = false;

    @XmlElement(name = "member")
    private List<MetaAttribute> attributes = new ArrayList<>(2);

    public String getClazz() {
        return clazz;
    }

    public void setClazz(final String clazz) {
        this.clazz = clazz;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(final boolean remove) {
        this.remove = remove;
    }

    public List<MetaAttribute> getAttributes() {
        return attributes;
    }
}
