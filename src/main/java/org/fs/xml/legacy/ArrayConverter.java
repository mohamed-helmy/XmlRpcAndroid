package org.fs.xml.legacy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Created by Fatih on 24/06/16.
 */
class ArrayConverter extends Converter<XMLEventReader, Collection<Object>> {

    public final static String ARRAY_NAME = "array";
    public final static String DATA_NAME  = "data";
    public final static String VALUE_NAME = "value";

    public static ArrayConverter create() {
        return new ArrayConverter();
    }

    private ArrayConverter() {}

    @Override public void write(XMLEventWriter writer, String prefix, String namespace, Collection<Object> collection) throws XMLStreamException {
        startElement(writer, prefix, namespace, ARRAY_NAME);
        startElement(writer, prefix, namespace, DATA_NAME);
        Iterator<Object> iter = collection.iterator();
        while(iter.hasNext()) {
            Object value = iter.next();
            Converter converter = Parser.findConverter(value);
            startElement(writer, prefix, namespace, VALUE_NAME);
            converter.write(writer, prefix, namespace, Parser.deepCast(value));
            endElement(writer, prefix, namespace, VALUE_NAME);
        }
        endElement(writer, prefix, namespace, DATA_NAME);
        endElement(writer, prefix, namespace, ARRAY_NAME);
    }

    @Override public Collection<Object> read(XMLEventReader reader) throws XMLStreamException {
        List<Object> array = new ArrayList<Object>();
        while(reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                final String nodeName = start.getName().getLocalPart();
                if (nodeName.equalsIgnoreCase(DATA_NAME)) {
                    continue;
                }
                if (nodeName.equalsIgnoreCase(VALUE_NAME)) {
                    event = reader.nextEvent();//data event
                    Converter converter = Parser.findConverter(event);
                    if (converter instanceof ArrayConverter || converter instanceof StructConverter) {
                        array.add(Parser.deepCast(converter.read(reader)));
                    } else {
                        array.add(Parser.deepCast(converter.read(reader.nextEvent())));
                    }
                    continue;
                }
            }
            if (event.isEndElement()) {
                EndElement end = event.asEndElement();
                final String endNodeName = end.getName().getLocalPart();
                if (endNodeName.equalsIgnoreCase(VALUE_NAME) || endNodeName.equalsIgnoreCase(DATA_NAME)) {
                    continue;
                }
                if (endNodeName.equalsIgnoreCase(ARRAY_NAME)) {
                    break;
                }
            }

        }
        return array;
    }

    @Override public boolean canRead(XMLEvent event) throws XMLStreamException {
        StartElement start = event.asStartElement();
        final String nodeName = start.getName().getLocalPart();
        return nodeName.equalsIgnoreCase(ARRAY_NAME);
    }

    @Override public boolean canWrite(Object o) {
        return o != null && o instanceof Collection;
    }
}
