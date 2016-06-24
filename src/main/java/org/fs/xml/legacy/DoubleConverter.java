package org.fs.xml.legacy;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Created by Fatih on 24/06/16.
 */
class DoubleConverter extends Converter<XMLEvent, Double> {

    public final static String DOUBLE_NAME = "double";

    public static DoubleConverter create() {
        return new DoubleConverter();
    }

    private DoubleConverter() {}

    @Override public void write(XMLEventWriter writer, String prefix, String namespace, Double value) throws XMLStreamException {
        startElement(writer, prefix, namespace, DOUBLE_NAME);
        charElement(writer, String.valueOf(value));
        endElement(writer, prefix, namespace, DOUBLE_NAME);
    }

    @Override public Double read(XMLEvent event) throws XMLStreamException {
        Characters chars = event.asCharacters();
        return Double.parseDouble(chars.getData());
    }

    @Override public boolean canRead(XMLEvent event) throws XMLStreamException {
        StartElement start = event.asStartElement();
        final String nodeName = start.getName().getLocalPart();
        return nodeName.equalsIgnoreCase(DOUBLE_NAME);
    }

    @Override public boolean canWrite(Object o) {
        return o != null && o instanceof Double;
    }
}
