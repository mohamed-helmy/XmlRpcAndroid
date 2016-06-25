package org.fs.xml.internal;

import org.fs.xml.internal.type.XMLRpcRequest;
import org.fs.xml.internal.type.XMLRpcResponse;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Fatih on 25/06/16.
 * as org.fs.xml.internal.Parser
 */
public final class Parser {

    private final static List<TypeParser<?>> converters;
    static {
        converters = new ArrayList<>();
        converters.add(Base64StringTypeParser.create());
        converters.add(IntegerTypeParser.create(Constants.INTEGER_V1));
        converters.add(IntegerTypeParser.create(Constants.INTEGER_V2));
        converters.add(IntegerTypeParser.create(Constants.INTEGER_V3));
        converters.add(LongTypeParser.create(Constants.LONG_V1));
        converters.add(LongTypeParser.create(Constants.LONG_V2));
        converters.add(CollectionTypeParser.create());
        converters.add(StructTypeParser.create());
        converters.add(DoubleTypeParser.create());
        converters.add(NullTypeParser.create());
        converters.add(FloatTypeParser.create());
        converters.add(XMLRpcRequestTypeParser.create());//request
        converters.add(XMLRpcResponseTypeParser.create());//response
    }

    public Parser() { }

    public void addStringConverter(boolean plain) {
        if (plain) {
            converters.add(StringTypeParser.create(StringTypeParser.STYLE_NO_WRAP));
        } else {
            converters.add(StringTypeParser.create(StringTypeParser.STYLE_WRAP));
        }
    }

    public void addBooleanConverter(boolean binary) {
        if (binary) {
            converters.add(BooleanTypeParser.create(BooleanTypeParser.STYLE_BINARY));
        } else {
            converters.add(BooleanTypeParser.create(BooleanTypeParser.STYLE_STRING));
        }
    }

    public void addDateConverter() {
        converters.add(DateTypeParser.create());
    }

    public void addDateConverter(String formatStr) {
        converters.add(DateTypeParser.create(formatStr));
    }

    public void addDateConverter(String formatStr, Locale locale) {
        converters.add(DateTypeParser.create(formatStr, locale));
    }

    public void addDateConverter(String formatStr, Locale locale, TimeZone timeZone) {
        converters.add(DateTypeParser.create(formatStr, locale, timeZone));
    }

    public void write(OutputStreamWriter writer, XMLRpcRequest request, String charSet) throws IOException {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlSerializer xmlWriter = factory.newSerializer();
            xmlWriter.setOutput(writer);
            xmlWriter.startDocument(charSet, null);//no standalone should be written
            xmlWriter.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);//pretty xml
            TypeParser converter = findWriteParser(request);
            converter.write(xmlWriter, request);
            xmlWriter.endDocument();//don't forget to end document
            xmlWriter.flush();//don't forget to flush
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public XMLRpcResponse read(InputStream in, String charSet) throws IOException {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xmlReader = factory.newPullParser();
            xmlReader.setInput(in, charSet);
            xmlReader.next();//start gone
            TypeParser converter = findReadParser(xmlReader);
            Object o = converter.read(xmlReader);
            return (XMLRpcResponse) o;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public static TypeParser findReadParser(XmlPullParser reader) {
        for (TypeParser converter : converters) {
            if (converter.hasRead(reader)) {
                return converter;
            }
        }
        throw new RuntimeException("no reader found for @{ " + reader.getName() + " }");
    }

    public static TypeParser findWriteParser(Object object) {
        for (TypeParser converter : converters) {
            if (converter.hasWrite(object)) {
                return converter;
            }
        }
        throw new RuntimeException("no writer found for @{ " + object.toString() + " }");
    }
}
