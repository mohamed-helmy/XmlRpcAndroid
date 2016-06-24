package org.fs.xml.legacy;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Created by Fatih on 24/06/16.
 */
class FloatConverter extends Converter<XMLEvent, Float> {

    public final static String FLOAT_NAME = "float";

    public static FloatConverter create() {
        return new FloatConverter();
    }

    private FloatConverter() { }

    @Override public void write(XMLEventWriter writer, String prefix, String namespace, Float value) throws XMLStreamException {
        startElement(writer, prefix, namespace, FLOAT_NAME);
        charElement(writer, String.valueOf(value));
        endElement(writer, prefix, namespace, FLOAT_NAME);
    }

    @Override public Float read(XMLEvent event) throws XMLStreamException {
        Characters chars = event.asCharacters();
        return Float.parseFloat(chars.getData());
    }

    @Override public boolean canRead(XMLEvent event) throws XMLStreamException {
        StartElement start = event.asStartElement();
        final String nodeName = start.getName().getLocalPart();
        return nodeName.equalsIgnoreCase(FLOAT_NAME);
    }

    @Override public boolean canWrite(Object o) {
        return o != null && o instanceof Float;
    }
}
