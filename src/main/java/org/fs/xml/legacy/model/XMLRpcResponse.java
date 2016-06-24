package org.fs.xml.legacy.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fatih on 24/06/16.
 */
public final class XMLRpcResponse {

    private List<Param> response;

    private Object fault;

    public XMLRpcResponse() { }

    public void setFault(Object fault) {
        this.fault = fault;
    }

    public void addParam(Param param) {
        if (response == null) {
            response = new ArrayList<Param>();
        }
        response.add(param);
    }

    public int paramSize() {
        return response != null ? response.size() : 0;
    }

    public Param paramAt(int index) {
        if (index >= 0 && index < paramSize()) {
            return response.get(index);
        }
        return null;
    }

    public Object getFault() {
        return fault;
    }

    public List<Param> getResponse() {
        return response;
    }

    public boolean isSuccess() {
        return fault == null;
    }
}
