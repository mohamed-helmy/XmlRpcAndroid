package org.fs.xml.legacy.model;

import android.util.Base64;

import java.nio.charset.Charset;

/**
 * Created by Fatih on 24/06/16.
 * as org.fs.xml.legacy.model.Base64String
 */
public final class Base64String {
    private String base64;

    public Base64String(String base64) {
        this.base64 = base64;
    }

    public String getBase64() {
        return base64;
    }

    public String decodedStr() {
        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        return new String(data, Charset.forName("UTF-8"));
    }

    public String encodedStr() {
        byte[] data = base64.getBytes(Charset.forName("UTF-8"));
        return Base64.encodeToString(data, Base64.DEFAULT);
    }
}
