package org.fs.xml.legacy;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.util.*;

/**
 * Created by Fatih on 24/06/16.
 */
class StructConverter extends Converter<XMLEventReader, Map<String, Object>> {

    public static final String STRUCT_NAME = "struct";
    public static final String MEMBER_NAME = "member";
    public static final String KEY_NAME    = "name";
    public static final String VALUE_NAME  = "value";

    public static StructConverter create() {
        return new StructConverter();
    }

    private StructConverter() { }

    @Override public void write(XMLEventWriter writer, String prefix, String namespace, Map<String, Object> value) throws XMLStreamException {
        startElement(writer, prefix, namespace, STRUCT_NAME);
        Set<String> keys = value.keySet();
        for(String key : keys) {
            startElement(writer, prefix, namespace, MEMBER_NAME);
            //key written
            startElement(writer, prefix, namespace, KEY_NAME);
            charElement(writer, key);
            endElement(writer, prefix, namespace, KEY_NAME);
           //value written
            startElement(writer, prefix, namespace, VALUE_NAME);
            Object o = value.get(key);
            Converter converter = Parser.findConverter(o);
            converter.write(writer, prefix, namespace, Parser.deepCast(o));
            endElement(writer, prefix, namespace, VALUE_NAME);
            endElement(writer, prefix, namespace, MEMBER_NAME);
        }
        endElement(writer, prefix, namespace, STRUCT_NAME);
    }

    @Override public Map<String, Object> read(XMLEventReader reader) throws XMLStreamException {
        Map<String, Object> map = new HashMap<String, Object>();
        String key = "";//never used this value
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                final String nodeName = start.getName().getLocalPart();
                if (nodeName.equalsIgnoreCase(MEMBER_NAME)) {
                    continue;
                }
                if (nodeName.equalsIgnoreCase(KEY_NAME)) {
                    event = reader.nextEvent();
                    key = event.asCharacters().getData();
                    continue;
                }
                if (nodeName.equalsIgnoreCase(VALUE_NAME)) {
                    event = reader.nextEvent();//data event
                    Converter converter = Parser.findConverter(event);
                    if (converter instanceof ArrayConverter || converter instanceof StructConverter) {
                        map.put(key, Parser.deepCast(converter.read(reader)));
                    } else {
                        map.put(key, Parser.deepCast(converter.read(reader.nextEvent())));
                    }
                    continue;
                }
            }
            if (event.isEndElement()) {
                EndElement end = event.asEndElement();
                final String endNodeName = end.getName().getLocalPart();
                if (endNodeName.equalsIgnoreCase(VALUE_NAME) || endNodeName.equalsIgnoreCase(MEMBER_NAME)) {
                    continue;
                }
                if (endNodeName.equalsIgnoreCase(STRUCT_NAME)) {
                    break;
                }
            }
        }
        return map;
    }

    @Override public boolean canRead(XMLEvent event) throws XMLStreamException {
        StartElement start = event.asStartElement();
        final String nodeName = start.getName().getLocalPart();
        return nodeName.equalsIgnoreCase(STRUCT_NAME);
    }

    @Override public boolean canWrite(Object o) {
        return o != null && o instanceof Map;
    }
}
