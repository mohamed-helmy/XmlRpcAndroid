package org.fs.xml.okhttp;


import org.fs.xml.transform.XMLRpcMatcher;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Fatih on 10/11/15.
 * as org.fs.xml.okhttp.SimpleXMLConverterFactory
 */
public class SimpleXMLConverterFactory extends Converter.Factory {

    public static SimpleXMLConverterFactory create() {
        return create(new Persister(new XMLRpcMatcher(), new Format("<?xml version=\"1.0\" encoding= \"UTF-8\" ?>")));
    }

    public static SimpleXMLConverterFactory create(Serializer serializer) {
        return new SimpleXMLConverterFactory(serializer, true);
    }

    public static SimpleXMLConverterFactory createNonStrict() {
        return create(new Persister(new XMLRpcMatcher(), new Format("<?xml version=\"1.0\" encoding= \"UTF-8\" ?>")));
    }

    public static SimpleXMLConverterFactory createNonStrict(Serializer serializer) {
        return new SimpleXMLConverterFactory(serializer, false);
    }

    private final Serializer serializer;
    private final boolean    strict;

    private SimpleXMLConverterFactory(Serializer serializer, boolean strict) {
        this.serializer = serializer;
        this.strict = strict;
    }

    public boolean isStrict() {
        return strict;
    }

    @Override public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if(!(type instanceof Class))
            return null;
        return new SimpleXmlResponseBodyConverter<>((Class<?>)type, serializer, strict);
    }

    @Override public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        if(!(type instanceof Class))
            return null;
        return new SimpleXmlRequestBodyConverter<>(serializer);
    }
}
