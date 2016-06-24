package org.fs.xml.legacy;

import org.fs.xml.legacy.model.Param;
import org.fs.xml.legacy.model.XMLRpcRequest;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;

/**
 * Created by Fatih on 24/06/16.
 */
class XMLRpcRequestConverter extends Converter<XMLEventReader, XMLRpcRequest> {

    public final static String METHOD_CALL = "methodCall";
    public final static String METHOD_NAME = "methodName";
    public final static String PARAMS      = "params";
    public final static String PARAM       = "param";
    public final static String VALUE       = "value";

    public static XMLRpcRequestConverter create() {
        return new XMLRpcRequestConverter();
    }

    private XMLRpcRequestConverter() { }

    @Override public void write(XMLEventWriter writer, String prefix, String namespace, XMLRpcRequest request) throws XMLStreamException {
        StartDocument start = factory.createStartDocument();
        writer.add(start);
        startElement(writer, prefix, namespace, METHOD_CALL);
        //method name added
        startElement(writer, prefix, namespace, METHOD_NAME);
        Characters chars = factory.createCharacters(request.getMethodName());
        writer.add(chars);
        endElement(writer, prefix, namespace, METHOD_NAME);
        //params
        startElement(writer, prefix, namespace, PARAMS);
        for (int i = 0, z = request.paramSize(); i < z; i++) {
            startElement(writer, prefix, namespace, PARAM);
            Param p = request.paramAt(i);
            StartElement valueStart = factory.createStartElement(prefix, namespace, VALUE);
            writer.add(valueStart);
            //write value
            Converter converter = Parser.findConverter(p.asNull());
            converter.write(writer, prefix, namespace, Parser.deepCast(p.asNull()));

            EndElement valueEnd = factory.createEndElement(prefix, namespace, VALUE);
            writer.add(valueEnd);
            endElement(writer, prefix, namespace, PARAM);
        }
        endElement(writer, prefix, namespace, METHOD_CALL);
        EndDocument end = factory.createEndDocument();
        writer.add(end);
        writer.close();
    }

    @Override public XMLRpcRequest read(XMLEventReader reader) throws XMLStreamException {
        XMLRpcRequest request = new XMLRpcRequest();
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                String nodeName = start.getName().getLocalPart();
                if (nodeName.equalsIgnoreCase(METHOD_CALL) || nodeName.equalsIgnoreCase(PARAMS)) {
                    continue;
                }
                if (nodeName.equalsIgnoreCase(METHOD_NAME)) {
                    event = reader.nextEvent();
                    request.setMethodName(event.asCharacters().getData());
                    continue;
                }
                if (nodeName.equalsIgnoreCase(PARAM)) {
                    event = reader.nextEvent();
                    if (event.isStartElement()) {
                        start = event.asStartElement();
                        nodeName = start.getName().getLocalPart();
                        if (nodeName.equalsIgnoreCase(VALUE)) {
                            event = reader.nextEvent();
                            Converter converter = Parser.findConverter(event);
                            if(converter instanceof ArrayConverter || converter instanceof StructConverter) {
                                request.addParam(new Param(converter.read(reader)));
                            } else {
                                request.addParam(new Param(converter.read(event)));
                            }
                            continue;
                        }
                    }
                }
            }
            if (event.isEndElement()) {
                EndElement end = event.asEndElement();
                final String nodeName = end.getName().getLocalPart();
                if (nodeName.equalsIgnoreCase(METHOD_CALL)) {
                    break;
                }
            }
        }
        return request;
    }

    @Override public boolean canRead(XMLEvent event) throws XMLStreamException {
        StartElement start = event.asStartElement();
        final String nodeName = start.getName().getLocalPart();
        return nodeName.equalsIgnoreCase(METHOD_CALL);
    }

    @Override public boolean canWrite(Object o) {
        return o != null && o instanceof XMLRpcRequest;
    }
}
