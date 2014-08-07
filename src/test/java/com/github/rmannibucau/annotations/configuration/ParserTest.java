package com.github.rmannibucau.annotations.configuration;

import com.github.rmannibucau.annotations.configuration.model.AClass;
import com.github.rmannibucau.annotations.configuration.model.DecoratedClass;
import com.github.rmannibucau.annotations.configuration.model.DecoratedClassWithOther;
import com.github.rmannibucau.annotations.configuration.model.annotation.FooField;
import com.github.rmannibucau.annotations.configuration.model.annotation.FooMethod;
import com.github.rmannibucau.annotations.configuration.model.annotation.FooType;
import org.junit.Test;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.ByteArrayInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ParserTest {
    @Test
    public void noAnnotationClass() {
        final Parser parser = new Parser();
        final ConfigurableClass clazz = parser.parse(AClass.class);
        assertThat(clazz).isNotNull();
        assertThat(clazz.fields()).hasSize(1);
        assertThat(clazz.methods()).hasSize(1);

        assertThat(clazz.getAnnotations()).hasSize(0);
        assertThat(clazz.getMethod("aMethod", String.class).getAnnotations()).hasSize(0);
        assertThat(clazz.methods().iterator().next().getAnnotations()).hasSize(0);
        assertThat(clazz.getField("aField").getAnnotations()).hasSize(0);
        assertThat(clazz.fields().iterator().next().getAnnotations()).hasSize(0);
    }

    @Test
    public void annotationClass() {
        final Parser parser = new Parser();
        final ConfigurableClass clazz = parser.parse(DecoratedClass.class);
        assertThat(clazz).isNotNull();
        assertThat(clazz.fields()).hasSize(1);
        assertThat(clazz.methods()).hasSize(1);

        assertThat(clazz.getAnnotations()).hasSize(1);
        assertThat(clazz.getMethod("aMethod", String.class).getAnnotations()).hasSize(1);
        assertThat(clazz.methods().iterator().next().getAnnotations()).hasSize(1);
        assertThat(clazz.getField("aField").getAnnotations()).hasSize(1);
        assertThat(clazz.fields().iterator().next().getAnnotations()).hasSize(1);
    }

    @Test
    public void simpleClassWithMeta() {
        final Parser parser = new Parser(new ByteArrayInputStream(("" +
                "<model>" +
                "   <class name=\"" + AClass.class.getName() + "\">" +
                "       <annotation class=\"" + FooType.class.getName() + "\">" +
                "           <member name=\"bar\" value=\"9\" />" +
                "           <member name=\"provider\" value=\"" + ParserTest.class.getName() + "\" />" +
                "           <member name=\"value\" value=\"V\" />" +
                "       </annotation>" +
                "       <method name=\"aMethod\">" +
                "           <annotation class=\"" + FooMethod.class.getName() + "\">" +
                "               <member name=\"bar\" value=\"2\" />" +
                "               <member name=\"provider\" value=\"" + Parser.class.getName() + "\" />" +
                "               <member name=\"value\" value=\"V2\" />" +
                "           </annotation>" +
                "       </method>" +
                "       <field name=\"aField\">" +
                "           <annotation class=\"" + FooField.class.getName() + "\">" +
                "               <member name=\"bar\" value=\"3\" />" +
                "               <member name=\"provider\" value=\"" + DecoratedClass.class.getName() + "\" />" +
                "               <member name=\"value\" value=\"V3\" />" +
                "           </annotation>" +
                "       </field>" +
                "   </class>" +
                "</model>" +
                "").getBytes()));
        final ConfigurableClass clazz = parser.parse(AClass.class);
        assertThat(clazz).isNotNull();
        assertThat(clazz.fields()).hasSize(1);
        assertThat(clazz.methods()).hasSize(1);

        assertThat(clazz.getAnnotations()).hasSize(1);
        final FooType type = clazz.getAnnotation(FooType.class);
        assertThat(type).isNotNull();
        assertThat(type.bar()).isEqualTo(9);
        assertThat(type.provider()).isEqualTo(ParserTest.class);
        assertThat(type.value()).isEqualTo("V");
        assertThat(clazz.getMethod("aMethod", String.class).getAnnotations()).hasSize(1);
        final FooMethod method = clazz.getMethod("aMethod", String.class).getAnnotation(FooMethod.class);
        assertThat(method).isNotNull();
        assertThat(method.bar()).isEqualTo(2);
        assertThat(method.provider()).isEqualTo(Parser.class);
        assertThat(method.value()).isEqualTo("V2");
        assertThat(clazz.methods().iterator().next().getAnnotations()).hasSize(1);
        assertThat(clazz.getField("aField").getAnnotations()).hasSize(1);
        assertThat(clazz.fields().iterator().next().getAnnotations()).hasSize(1);
        final FooField field = clazz.getField("aField").getAnnotation(FooField.class);
        assertThat(field).isNotNull();
        assertThat(field.bar()).isEqualTo(3);
        assertThat(field.provider()).isEqualTo(DecoratedClass.class);
        assertThat(field.value()).isEqualTo("V3");
    }

    @Test
    public void alreadyDecoratedClassWithMetaOverriding() {
        final Parser parser = new Parser(new ByteArrayInputStream(("" +
                "<model>" +
                "   <class name=\"" + DecoratedClass.class.getName() + "\">" +
                "       <annotation class=\"" + FooType.class.getName() + "\">" +
                "           <member name=\"bar\" value=\"9\" />" +
                "           <member name=\"provider\" value=\"" + ParserTest.class.getName() + "\" />" +
                "           <member name=\"value\" value=\"V\" />" +
                "       </annotation>" +
                "       <method name=\"aMethod\">" +
                "           <annotation class=\"" + FooMethod.class.getName() + "\">" +
                "               <member name=\"bar\" value=\"2\" />" +
                "               <member name=\"provider\" value=\"" + Parser.class.getName() + "\" />" +
                "               <member name=\"value\" value=\"V2\" />" +
                "           </annotation>" +
                "       </method>" +
                "       <field name=\"aField\">" +
                "           <annotation class=\"" + FooField.class.getName() + "\">" +
                "               <member name=\"bar\" value=\"3\" />" +
                "               <member name=\"provider\" value=\"" + DecoratedClass.class.getName() + "\" />" +
                "               <member name=\"value\" value=\"V3\" />" +
                "           </annotation>" +
                "       </field>" +
                "   </class>" +
                "</model>" +
                "").getBytes()));
        final ConfigurableClass clazz = parser.parse(DecoratedClass.class);
        assertThat(clazz).isNotNull();
        assertThat(clazz.fields()).hasSize(1);
        assertThat(clazz.methods()).hasSize(1);

        assertThat(clazz.getAnnotations()).hasSize(1);
        final FooType type = clazz.getAnnotation(FooType.class);
        assertThat(type).isNotNull();
        assertThat(type.bar()).isEqualTo(9);
        assertThat(type.provider()).isEqualTo(ParserTest.class);
        assertThat(type.value()).isEqualTo("V");
        assertThat(clazz.getMethod("aMethod", String.class).getAnnotations()).hasSize(1);
        final FooMethod method = clazz.getMethod("aMethod", String.class).getAnnotation(FooMethod.class);
        assertThat(method).isNotNull();
        assertThat(method.bar()).isEqualTo(2);
        assertThat(method.provider()).isEqualTo(Parser.class);
        assertThat(method.value()).isEqualTo("V2");
        assertThat(clazz.methods().iterator().next().getAnnotations()).hasSize(1);
        assertThat(clazz.getField("aField").getAnnotations()).hasSize(1);
        assertThat(clazz.fields().iterator().next().getAnnotations()).hasSize(1);
        final FooField field = clazz.getField("aField").getAnnotation(FooField.class);
        assertThat(field).isNotNull();
        assertThat(field.bar()).isEqualTo(3);
        assertThat(field.provider()).isEqualTo(DecoratedClass.class);
        assertThat(field.value()).isEqualTo("V3");
    }

    @Test
    public void alreadyDecoratedClassWithMetaMerge() {
        final Parser parser = new Parser(new ByteArrayInputStream(("" +
                "<model>" +
                "   <class name=\"" + DecoratedClassWithOther.class.getName() + "\">" +
                "       <annotation class=\"" + FooType.class.getName() + "\">" +
                "           <member name=\"bar\" value=\"9\" />" +
                "           <member name=\"provider\" value=\"" + ParserTest.class.getName() + "\" />" +
                "           <member name=\"value\" value=\"V\" />" +
                "       </annotation>" +
                "       <method name=\"aMethod\">" +
                "           <annotation class=\"" + FooMethod.class.getName() + "\">" +
                "               <member name=\"bar\" value=\"2\" />" +
                "               <member name=\"provider\" value=\"" + Parser.class.getName() + "\" />" +
                "               <member name=\"value\" value=\"V2\" />" +
                "           </annotation>" +
                "       </method>" +
                "       <field name=\"aField\">" +
                "           <annotation class=\"" + FooField.class.getName() + "\">" +
                "               <member name=\"bar\" value=\"3\" />" +
                "               <member name=\"provider\" value=\"" + DecoratedClass.class.getName() + "\" />" +
                "               <member name=\"value\" value=\"V3\" />" +
                "           </annotation>" +
                "       </field>" +
                "   </class>" +
                "</model>" +
                "").getBytes()));
        final ConfigurableClass clazz = parser.parse(DecoratedClassWithOther.class);
        assertThat(clazz).isNotNull();
        assertThat(clazz.fields()).hasSize(1);
        assertThat(clazz.methods()).hasSize(1);

        assertThat(clazz.getAnnotations()).hasSize(2);
        assertThat(clazz.getAnnotation(XmlType.class)).isNotNull();
        final FooType type = clazz.getAnnotation(FooType.class);
        assertThat(type).isNotNull();
        assertThat(type.bar()).isEqualTo(9);
        assertThat(type.provider()).isEqualTo(ParserTest.class);
        assertThat(type.value()).isEqualTo("V");

        assertThat(clazz.getMethod("aMethod", String.class).getAnnotations()).hasSize(2);
        assertThat(clazz.getMethod("aMethod", String.class).getAnnotation(XmlElement.class)).isNotNull();
        final FooMethod method = clazz.getMethod("aMethod", String.class).getAnnotation(FooMethod.class);
        assertThat(method).isNotNull();
        assertThat(method.bar()).isEqualTo(2);
        assertThat(method.provider()).isEqualTo(Parser.class);
        assertThat(method.value()).isEqualTo("V2");
        assertThat(clazz.methods().iterator().next().getAnnotations()).hasSize(2);

        assertThat(clazz.getField("aField").getAnnotations()).hasSize(2);
        assertThat(clazz.fields().iterator().next().getAnnotations()).hasSize(2);
        assertThat(clazz.getField("aField").getAnnotation(XmlElement.class)).isNotNull();
        final FooField field = clazz.getField("aField").getAnnotation(FooField.class);
        assertThat(field).isNotNull();
        assertThat(field.bar()).isEqualTo(3);
        assertThat(field.provider()).isEqualTo(DecoratedClass.class);
        assertThat(field.value()).isEqualTo("V3");
    }

    @Test
    public void remove() {
        final Parser parser = new Parser(new ByteArrayInputStream(("" +
                "<model>" +
                "   <class name=\"" + DecoratedClassWithOther.class.getName() + "\">" +
                "       <annotation remove=\"true\" class=\"" + XmlType.class.getName() + "\" />" +
                "   </class>" +
                "</model>" +
                "").getBytes()));
        final ConfigurableClass clazz = parser.parse(DecoratedClassWithOther.class);

        assertThat(clazz.getAnnotations()).hasSize(0); // XmlType is removed

        assertThat(clazz.getMethod("aMethod", String.class).getAnnotations()).hasSize(1);
        assertThat(clazz.getMethod("aMethod", String.class).getAnnotation(XmlElement.class)).isNotNull();

        assertThat(clazz.getField("aField").getAnnotations()).hasSize(1);
        assertThat(clazz.getField("aField").getAnnotation(XmlElement.class)).isNotNull();
    }
}
