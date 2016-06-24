package org.fs.xml.okhttp;

import org.fs.xml.legacy.Parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Fatih on 24/06/16.
 * as org.fs.xml.okhttp.LegacyConverterFactory
 */
public class LegacyConverterFactory extends Converter.Factory {

    private final Parser parser;

    public static LegacyConverterFactory create() {
        return create("yyyyMMdd'T'HH:mm:ss", Locale.getDefault());
    }

    public static LegacyConverterFactory create(String formatStr, Locale locale) {
        return create(formatStr, locale, TimeZone.getTimeZone("GMT"));
    }

    public static LegacyConverterFactory create(String formatStr, Locale locale, TimeZone zone) {
        return new LegacyConverterFactory(formatStr, locale, zone);
    }

    private LegacyConverterFactory(String format, Locale locale, TimeZone timeZone) {
        this.parser = new Parser();
        parser.addDateFormat(format, locale, timeZone);
    }

    @Override public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new LegacyRequestBodyConverter(parser);
    }

    @Override public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new LegacyResponseBodyConverter(parser);
    }
}
