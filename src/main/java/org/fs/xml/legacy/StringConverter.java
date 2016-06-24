package org.fs.xml.legacy;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Created by Fatih on 24/06/16.
 */
class StringConverter extends Converter<XMLEvent, String> {

    public final static String STRING_NAME = "string";

    public static StringConverter create() {
        return new StringConverter();
    }

    private StringConverter() { }

    @Override public void write(XMLEventWriter writer, String prefix, String namespace, String value) throws XMLStreamException {
        startElement(writer, prefix, namespace, STRING_NAME);
        charElement(writer, value);
        endElement(writer, prefix, namespace, STRING_NAME);
    }

    @Override public String read(XMLEvent event) throws XMLStreamException {
        Characters chars = event.asCharacters();
        return chars.getData();
    }

    @Override public boolean canRead(XMLEvent event) throws XMLStreamException {
        StartElement start = event.asStartElement();
        final String nodeName = start.getName().getLocalPart();
        return nodeName.equalsIgnoreCase(STRING_NAME);
    }

    @Override public boolean canWrite(Object o) {
        return o != null && o instanceof String;
    }
}
