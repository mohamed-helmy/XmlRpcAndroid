package org.fs.xml.legacy;

import org.fs.xml.legacy.model.Param;
import org.fs.xml.legacy.model.XMLRpcResponse;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;

/**
 * Created by Fatih on 24/06/16.
 */
class XMLRpcResponseConverter extends Converter<XMLEventReader, XMLRpcResponse> {

    public final static String METHOD_RESPONSE = "methodResponse";
    public final static String PARAMS          = "params";
    public final static String PARAM           = "param";

    public final static String FAULT           = "fault";
    public final static String VALUE           = "value";

    public static XMLRpcResponseConverter create() {
        return new XMLRpcResponseConverter();
    }

    private XMLRpcResponseConverter() { }

    @Override public void write(XMLEventWriter writer, String prefix, String namespace, XMLRpcResponse response) throws XMLStreamException {
        StartDocument start = factory.createStartDocument();
        writer.add(start);
        startElement(writer, prefix, namespace, METHOD_RESPONSE);
        if (response.isSuccess()) {
            startElement(writer, prefix, namespace, PARAMS);
            for (int i = 0, z = response.paramSize(); i < z; i++) {
                startElement(writer, prefix, namespace, PARAM);
                Param p = response.paramAt(i);
                startElement(writer, prefix, namespace, VALUE);
                //write value
                Converter converter = Parser.findConverter(p.asNull());
                converter.write(writer, prefix, namespace, Parser.deepCast(p.asNull()));

                endElement(writer, prefix, namespace, VALUE);
                endElement(writer, prefix, namespace, PARAM);
            }
            endElement(writer, prefix, namespace, PARAMS);
        } else {
            startElement(writer, prefix, namespace, FAULT);
            startElement(writer, prefix, namespace, VALUE);

            Converter converter = Parser.findConverter(response.getFault());
            converter.write(writer, prefix, namespace, Parser.deepCast(response.getFault()));

            endElement(writer, prefix, namespace, VALUE);
            endElement(writer, prefix, namespace, FAULT);
        }
        endElement(writer, prefix, namespace, METHOD_RESPONSE);
        EndDocument end = factory.createEndDocument();
        writer.add(end);
        writer.close();
    }

    @Override public XMLRpcResponse read(XMLEventReader reader) throws XMLStreamException {
        XMLRpcResponse response = new XMLRpcResponse();
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                String nodeName = start.getName().getLocalPart();
                if (nodeName.equalsIgnoreCase(PARAMS) || nodeName.equalsIgnoreCase(METHOD_RESPONSE) ) {
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
                                response.addParam(new Param(converter.read(reader)));
                            } else {
                                response.addParam(new Param(converter.read(event)));
                            }
                            continue;
                        }
                    }
                }
                if (nodeName.equalsIgnoreCase(FAULT)) {
                    event = reader.nextEvent();
                    if (event.isStartElement()) {
                        start = event.asStartElement();
                        nodeName = start.getName().getLocalPart();
                        if (nodeName.equalsIgnoreCase(VALUE)) {
                            event = reader.nextEvent();
                            Converter converter = Parser.findConverter(event);
                            if(converter instanceof ArrayConverter || converter instanceof StructConverter) {
                                response.setFault(converter.read(reader));
                            } else {
                                response.setFault(converter.read(event));
                            }
                            continue;
                        }
                    }
                    continue;
                }
            }
            if (event.isEndElement()) {
                 EndElement end = event.asEndElement();
                final String nodeName = end.getName().getLocalPart();
                if (nodeName.equalsIgnoreCase(METHOD_RESPONSE)) {
                    break;
                }
            }
        }
        return response;
    }

    @Override public boolean canRead(XMLEvent event) throws XMLStreamException {
        StartElement start = event.asStartElement();
        final String nodeName = start.getName().getLocalPart();
        return nodeName.equalsIgnoreCase(METHOD_RESPONSE);
    }

    @Override public boolean canWrite(Object o) {
        return o != null && o instanceof XMLRpcResponse;
    }
}
