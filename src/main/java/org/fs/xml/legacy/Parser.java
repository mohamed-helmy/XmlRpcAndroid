package org.fs.xml.legacy;

import org.fs.xml.legacy.model.XMLRpcRequest;
import org.fs.xml.legacy.model.XMLRpcResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import javanet.staxutils.IndentingXMLEventWriter;

/**
 * Created by Fatih on 24/06/16.
 */
public class Parser {

    private final static String EMPTY_PREFIX    = "";
    private final static String EMPTY_NAMESPACE = "";

    private final static List<Converter<?, ?>> converters;
    static {
        converters = new ArrayList<>();
        converters.add(NullConverter.create());
        converters.add(IntegerConverter.create(IntegerConverter.INTEGER_NAME_V1));
        converters.add(IntegerConverter.create(IntegerConverter.INTEGER_NAME_V2));
        converters.add(LongConverter.create(LongConverter.LONG_NAME_V1));
        converters.add(LongConverter.create(LongConverter.LONG_NAME_V2));
        converters.add(DoubleConverter.create());
        converters.add(FloatConverter.create());
        converters.add(StringConverter.create());
        converters.add(ArrayConverter.create());
        converters.add(StructConverter.create());
        converters.add(XMLRpcRequestConverter.create());
        converters.add(XMLRpcResponseConverter.create());
    }

    public Parser() { }

    public void addConverter(Converter<?, ?> converter) {
        converters.add(converter);
    }

    public void addDateFormat(String format, Locale locale) {
        addConverter(DateConverter.create(format, locale));
    }

    public void addDateFormat(String format, Locale locale, TimeZone zone) {
        DateConverter converter = DateConverter.create(format, locale);
        converter.setTimeZone(zone);
        addConverter(converter);
    }

    public void write(OutputStreamWriter out, XMLRpcRequest request) throws IOException {
        write(out, EMPTY_PREFIX, EMPTY_NAMESPACE, request);
    }

    public void write(OutputStreamWriter out, String prefix, String namespace, XMLRpcRequest request) throws IOException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        try {
            XMLEventWriter writer = factory.createXMLEventWriter(out);
            writer = new IndentingXMLEventWriter(writer);
            Converter converter = findConverter(request);
            converter.write(writer, prefix, namespace, deepCast(request));
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    public XMLRpcResponse read(InputStream in) throws IOException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        try {
            factory.setProperty(XMLInputFactory.SUPPORT_DTD, true);
            XMLEventReader reader = factory.createXMLEventReader(in);
            //clean up whitespace
            reader = factory.createFilteredReader(reader, new WhiteSpaceFilter());
            //read startDocument crap
            XMLEvent event = reader.hasNext() ? reader.nextEvent(): null;
            if (event != null && event.isStartDocument()) {
                //then peek into very first element
                Converter converter = findConverter(reader.hasNext() ? reader.peek() : null);
                return deepCast(converter.read(reader));
            }
            return null;
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    public static Converter findConverter(Object object) {
        for (Converter<?, ?> converter : converters) {
            if (converter.canWrite(object)) {
                return (Converter) converter;
            }
        }
        throw new UnsupportedOperationException("we can not find relative converter");
    }

    public static Converter findConverter(XMLEvent event) throws XMLStreamException {
        for (Converter<?, ?> converter : converters) {
            if (converter.canRead(event)) {
                return (Converter) converter;
            }
        }
        throw new UnsupportedOperationException("we can not find relative converter");
    }

    public static <V> V deepCast(Object o) {
        return (V) o;
    }

}
