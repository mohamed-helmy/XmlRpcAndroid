package org.fs.xml.legacy;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Created by Fatih on 24/06/16.
 */
class IntegerConverter extends Converter<XMLEvent, Integer> {

    public final static String INTEGER_NAME_V1 = "i4";
    public final static String INTEGER_NAME_V2 = "int";

    private final String preferred;

    public static IntegerConverter create(String preferred) {
        return new IntegerConverter(preferred);
    }

    private IntegerConverter(String preferred) {
        this.preferred = preferred;
        boolean hasValidPreference = preferred.equalsIgnoreCase(INTEGER_NAME_V1) || preferred.equalsIgnoreCase(INTEGER_NAME_V2);
        if (!hasValidPreference) {
            throw new IllegalArgumentException("i4 or int can be selected");
        }
    }

    @Override public void write(XMLEventWriter writer, String prefix, String namespace, Integer value) throws XMLStreamException {
        startElement(writer, prefix, namespace, preferred);
        charElement(writer, String.valueOf(value));
        endElement(writer, prefix, namespace, preferred);
    }

    @Override public Integer read(XMLEvent event) throws XMLStreamException {
        Characters chars = event.asCharacters();
        return Integer.parseInt(chars.getData());
    }

    @Override public boolean canRead(XMLEvent event) throws XMLStreamException {
        StartElement start = event.asStartElement();
        final String nodeName = start.getName().getLocalPart();
        return nodeName.equalsIgnoreCase(preferred);
    }

    @Override public boolean canWrite(Object o) {
        return o != null && o instanceof Integer;
    }
}
