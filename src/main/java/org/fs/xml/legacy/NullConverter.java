package org.fs.xml.legacy;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Created by Fatih on 24/06/16.
 */
class NullConverter extends Converter<XMLEvent, Object> {

    public final static String NULL_NAME = "nil";

    public static NullConverter create() {
        return new NullConverter();
    }

    private NullConverter() {}

    @Override public void write(XMLEventWriter writer, String prefix, String namespace, Object value) throws XMLStreamException {
        endElement(writer, prefix, namespace, NULL_NAME);
    }

    @Override public Object read(XMLEvent event) throws XMLStreamException {
        return null;
    }

    @Override public boolean canRead(XMLEvent event) throws XMLStreamException {
        StartElement start = event.asStartElement();
        final String nodeName = start.getName().getLocalPart();
        return nodeName.equalsIgnoreCase(NULL_NAME);
    }

    @Override public boolean canWrite(Object o) {
        return o == null;
    }
}
