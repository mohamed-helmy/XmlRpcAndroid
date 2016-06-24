package org.fs.xml.legacy;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Created by Fatih on 24/06/16.
 */
class LongConverter extends Converter<XMLEvent, Long> {

    public final static String LONG_NAME_V1 = "i8";
    public final static String LONG_NAME_V2 = "long";

    private final String preferred;

    public static LongConverter create(String preferred) {
        return new LongConverter(preferred);
    }

    private LongConverter(String preferred) {
        this.preferred = preferred;
        boolean hasValidType = preferred.equalsIgnoreCase(LONG_NAME_V1) || preferred.equalsIgnoreCase(LONG_NAME_V2);
        if (!hasValidType) {
            throw new IllegalArgumentException("only i8 or long");
        }
    }

    @Override public void write(XMLEventWriter writer, String prefix, String namespace, Long value) throws XMLStreamException {
        startElement(writer, prefix, namespace, preferred);
        charElement(writer, String.valueOf(value));
        endElement(writer, prefix, namespace, preferred);
    }

    @Override public Long read(XMLEvent event) throws XMLStreamException {
        Characters chars = event.asCharacters();
        return Long.parseLong(chars.getData());
    }

    @Override public boolean canRead(XMLEvent event) throws XMLStreamException {
        StartElement start = event.asStartElement();
        final String nodeName = start.getName().getLocalPart();
        return nodeName.equalsIgnoreCase(preferred);
    }

    @Override public boolean canWrite(Object o) {
        return o != null && o instanceof Long;
    }
}
