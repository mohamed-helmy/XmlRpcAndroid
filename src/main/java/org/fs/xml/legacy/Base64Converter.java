package org.fs.xml.legacy;

import org.fs.xml.legacy.model.Base64String;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Created by Fatih on 24/06/16.
 */
class Base64Converter extends Converter<XMLEvent, Base64String> {

    public final static String BASE64_NAME = "base64";

    public static Base64Converter create() {
        return new Base64Converter();
    }

    private Base64Converter() {}

    @Override public void write(XMLEventWriter writer, String prefix, String namespace, Base64String value) throws XMLStreamException {
        startElement(writer, prefix, namespace, BASE64_NAME);
        charElement(writer, value.decodedStr());
        endElement(writer, prefix, namespace, BASE64_NAME);
    }

    @Override public Base64String read(XMLEvent event) throws XMLStreamException {
        Characters chars = event.asCharacters();
        return new Base64String(chars.toString());
    }

    @Override public boolean canRead(XMLEvent event) throws XMLStreamException {
        StartElement start = event.asStartElement();
        final String nodeName = start.getName().getLocalPart();
        return nodeName.equalsIgnoreCase(BASE64_NAME);
    }

    @Override public boolean canWrite(Object o) {
        return o != null && o instanceof Base64String;
    }
}
