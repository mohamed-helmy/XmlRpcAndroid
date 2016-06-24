package org.fs.xml.legacy.model;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * Created by Fatih on 24/06/16.
 */
public final class Param {

    final Object object;

    public Param(Object object) {
        this.object = object;
    }

    public String asString() {
        return (String) object;
    }

    public Base64String asBase64String() {
        return (Base64String) object;
    }

    public Map<String, Object> asStruct() {
        return (Map<String, Object>) object;
    }

    public Collection<Object> asArray() {
        return (Collection<Object>) object;
    }

    public boolean asBoolean() {
        return (Boolean) object;
    }

    public int asInteger() {
        return (Integer) object;
    }

    public double asDouble() {
        return (Double) object;
    }

    public float asFloat() {
        return (Float) object;
    }

    public Object asNull() {
        return object;
    }

    public Date asDate() {
        return (Date) object;
    }
}
