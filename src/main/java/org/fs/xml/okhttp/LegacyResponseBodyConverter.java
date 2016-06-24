package org.fs.xml.okhttp;


import org.fs.xml.legacy.Parser;
import org.fs.xml.legacy.model.XMLRpcResponse;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Fatih on 24/06/16.
 * as org.fs.xml.okhttp.LegacyResponseBodyConverter
 */
public class LegacyResponseBodyConverter implements Converter<ResponseBody, XMLRpcResponse> {

    final Parser parser;

    public LegacyResponseBodyConverter(Parser parser) {
        this.parser = parser;
    }

    @Override public XMLRpcResponse convert(ResponseBody value) throws IOException {
        InputStream in = value.byteStream();
        try {
            return parser.read(in);
        } finally {
            in.close();
        }
    }
}
