package org.fs.xml.legacy;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Created by Fatih on 23/06/16.
 */
public abstract class Converter<T, V> {

    protected final XMLEventFactory factory         = XMLEventFactory.newFactory();
    protected final XMLEvent        newLineEvent    = factory.createDTD("\n");
    protected final XMLEvent        spaceEvent      = factory.createDTD("\t");

    abstract public void    write(XMLEventWriter writer, String prefix, String namespace,  V value) throws XMLStreamException;
    abstract public V       read(T event) throws XMLStreamException;
    abstract public boolean canRead(XMLEvent event) throws XMLStreamException;
    abstract public boolean canWrite(Object o);

    protected void startElement(XMLEventWriter writer, String prefix, String namespace, String typeName) throws XMLStreamException {
        StartElement start = factory.createStartElement(prefix, namespace, typeName);
        writer.add(start);
    }

    protected void endElement(XMLEventWriter writer, String prefix, String namespace, String typeName) throws XMLStreamException {
        EndElement end = factory.createEndElement(prefix, namespace, typeName);
        writer.add(end);
    }

    protected void charElement(XMLEventWriter writer, String str) throws XMLStreamException {
        Characters chars = factory.createCharacters(str);
        writer.add(chars);
    }
}
