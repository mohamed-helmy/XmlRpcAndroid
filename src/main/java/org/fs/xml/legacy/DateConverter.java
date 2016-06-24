package org.fs.xml.legacy;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Fatih on 24/06/16.
 */
class DateConverter extends Converter<XMLEvent, Date> {

    public final static String DATE_NAME = "dateTime.iso8601";

    private final SimpleDateFormat simpleDateFormat;

    public static DateConverter create(String formatStr, Locale locale) {
        return  new DateConverter(formatStr, locale);
    }

    private DateConverter(String formatStr, Locale locale) {
        simpleDateFormat = new SimpleDateFormat(formatStr, locale);
    }

    public void setTimeZone(TimeZone timeZone) {
        simpleDateFormat.setTimeZone(timeZone);
    }

    @Override public void write(XMLEventWriter writer, String prefix, String namespace, Date value) throws XMLStreamException {
        startElement(writer, prefix, namespace, DATE_NAME);
        charElement(writer, simpleDateFormat.format(value));
        endElement(writer, prefix, namespace, DATE_NAME);
    }

    @Override public Date read(XMLEvent event) throws XMLStreamException {
        Characters chars = event.asCharacters();
        try {
            return simpleDateFormat.parse(chars.getData());
        } catch (ParseException parse) {
            throw new XMLStreamException(parse);
        }
    }

    @Override public boolean canRead(XMLEvent event) throws XMLStreamException {
        StartElement start = event.asStartElement();
        final String nodeName = start.getName().getLocalPart();
        return nodeName.equalsIgnoreCase(DATE_NAME);
    }

    @Override public boolean canWrite(Object o) {
        return o != null && o instanceof Date;
    }
}
