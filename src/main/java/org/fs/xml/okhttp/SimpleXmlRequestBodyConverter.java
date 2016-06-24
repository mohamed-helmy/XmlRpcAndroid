package org.fs.xml.okhttp;


import org.simpleframework.xml.Serializer;

import java.io.IOException;
import java.io.OutputStreamWriter;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Converter;

/**
 * Created by Fatih on 10/11/15.
 * as org.fs.xml.okhttp.SimpleXmlRequestBodyConverter
 */
public class SimpleXmlRequestBodyConverter<T> implements Converter<T, RequestBody> {

    private final static MediaType MEDIA_TYPE = MediaType.parse("application/xml; charset=UTF-8");
    private final static String    UTF_8      = "UTF-8";

    private final Serializer serializer;

    public SimpleXmlRequestBodyConverter(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        Buffer buffer = new Buffer();
        try {
            OutputStreamWriter out = new OutputStreamWriter(buffer.outputStream(), UTF_8);
            serializer.write(value, out);
            out.flush();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
        return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
    }
}
