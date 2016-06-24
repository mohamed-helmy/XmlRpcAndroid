package org.fs.xml.okhttp;

import org.fs.xml.legacy.Parser;
import org.fs.xml.legacy.model.XMLRpcRequest;

import java.io.IOException;
import java.io.OutputStreamWriter;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Converter;

/**
 * Created by Fatih on 24/06/16.
 * as org.fs.xml.okhttp.LegacyRequestBodyConverter
 */
public class LegacyRequestBodyConverter implements Converter<XMLRpcRequest, RequestBody> {

    private final static MediaType MEDIA_TYPE = MediaType.parse("text/xml; charset=UTF-8");
    private final static String    UTF_8      = "UTF-8";

    private final Parser parser;

    public LegacyRequestBodyConverter(Parser parser) {
        this.parser = parser;
    }

    @Override public RequestBody convert(XMLRpcRequest value) throws IOException {
        Buffer buffer = new Buffer();
        OutputStreamWriter writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
        try {
            parser.write(writer, value);
            writer.flush();
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
        } finally {
            writer.close();
        }
    }
}
