package org.fs.xml.legacy.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fatih on 24/06/16.
 */
public final class XMLRpcRequest {

    private String methodName;

    private List<Param> params;

    public XMLRpcRequest()  { }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void addParam(Param param) {
        if (params == null) {
            params = new ArrayList<Param>();
        }
        params.add(param);
    }

    public String getMethodName() {
        return methodName;
    }

    public int paramSize() {
        return params != null ? params.size() : 0;
    }

    public Param paramAt(int index) {
        if (index >= 0 && index < paramSize()) {
            return params.get(index);
        }
        return null;
    }
}
