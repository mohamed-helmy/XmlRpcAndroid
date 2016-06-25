package org.fs.xml.internal;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

/**
 * Created by Fatih on 25/06/16.
 * as org.fs.xml.internal.TypeParser
 */
interface TypeParser<V> {

    void     write(XmlSerializer writer, V value) throws IOException;
    V        read(XmlPullParser reader) throws XmlPullParserException, IOException;
    boolean  hasRead(XmlPullParser reader);
    boolean  hasWrite(Object o);
}
