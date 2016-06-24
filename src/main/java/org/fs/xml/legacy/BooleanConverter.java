package org.fs.xml.legacy;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Created by Fatih on 24/06/16.
 */
class BooleanConverter extends Converter<XMLEvent, Boolean> {

    public static final int TYPE_BYTE = 0x01;
    public static final int TYPE_STR  = 0x02;

    public final static String BOOLEAN_NAME = "boolean";

    private final int type;

    public static BooleanConverter create(int type) {
        return new BooleanConverter(type);
    }

    private BooleanConverter(final int type) {
        this.type = type;
    }

    @Override public void write(XMLEventWriter writer, String prefix, String namespace, Boolean value) throws XMLStreamException {
        startElement(writer, prefix, namespace, BOOLEAN_NAME);
        if (type == TYPE_BYTE) {
            charElement(writer, String.valueOf(value ? 1 : 0));
        } else if (type == TYPE_STR) {
            charElement(writer, String.valueOf(value));
        } else {
            throw new XMLStreamException("unsupported boolean conversion");
        }
        endElement(writer, prefix, namespace, BOOLEAN_NAME);
    }

    @Override public boolean canRead(XMLEvent event) throws XMLStreamException {
        StartElement start = event.asStartElement();
        final String nodeName = start.getName().getLocalPart();
        return nodeName.equalsIgnoreCase(BOOLEAN_NAME);
    }

    @Override public Boolean read(XMLEvent event) throws XMLStreamException {
        Characters chars = event.asCharacters();
        if (type == TYPE_BYTE) {
            int byteFlag = Integer.parseInt(chars.getData());
            return byteFlag == 1;
        } else if (type == TYPE_STR) {
            return Boolean.valueOf(chars.getData());
        } else {
            throw new XMLStreamException("unsupported boolean conversion");
        }
    }

    @Override public boolean canWrite(Object o) {
        return o != null && o instanceof Boolean;
    }
}
