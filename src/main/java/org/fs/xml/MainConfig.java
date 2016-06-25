package org.fs.xml;

import org.fs.xml.internal.Parser;
import org.fs.xml.internal.type.Parameter;
import org.fs.xml.internal.type.XMLRpcRequest;

import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

import okio.Buffer;

/**
 * Created by Fatih on 25/06/16.
 * as org.fs.xml.MainConfig
 */
public class MainConfig {

    public static void main(String... args) throws Exception {
        XMLRpcRequest request = XMLRpcRequest.create("sayHello");
        Map<String, Object> params = new HashMap<>();
        params.put("a", 1);
        params.put("b", 2.0f);
        params.put("c", true);
        params.put("d", new Date());
        params.put("e", Arrays.asList(1, 2d, 3, true, "yahoo"));
        request.addParameter(Parameter.create(params));

        Buffer buffer = new Buffer();
        OutputStreamWriter writer = new OutputStreamWriter(buffer.outputStream(), "UTF-8");
        Parser parser = new Parser();
        parser.write(writer, request);
        writer.flush();
        System.out.print(new String(buffer.readByteArray(), Charset.forName("UTF-8")));
    }
}
