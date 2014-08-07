package com.github.rmannibucau.annotations.configuration;

import com.github.rmannibucau.annotations.configuration.xml.MetaModel;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;

public class Parser {
    private static final JAXBContext CONTEXT;
    static {
        try {
            CONTEXT = JAXBContext.newInstance(MetaModel.class);
        } catch (final JAXBException e) {
            throw new IllegalStateException(e);
        }
    }

    private final MetaModel model;

    public Parser() {
        this(null);
    }

    // you have to close the stream yourself, since we don't create it we don't handle it
    public Parser(final InputStream resourceAsStream) {
        if (resourceAsStream != null) {
            try {
                this.model = CONTEXT.createUnmarshaller().unmarshal(new StreamSource(resourceAsStream), MetaModel.class).getValue();
            } catch (final JAXBException e) {
                throw new IllegalArgumentException(e);
            }
        } else {
            this.model = new MetaModel();
        }
    }

    public MetaModel getModel() {
        return model;
    }

    public ConfigurableClass parse(final Class<?> clazz) {
        return new ConfigurableClass(clazz, model.getMeta(clazz.getName()));
    }
}
