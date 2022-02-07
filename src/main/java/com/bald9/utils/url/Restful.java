package com.bald9.utils.url;

import java.util.LinkedHashMap;

/**
 * @author bald9
 * @create 2022-02-04 21:02
 */
public class Restful {
    private int status;
    private LinkedHashMap<String,Object> result;

    @Override
    public String toString() {
        return "Restful{" +
                "status=" + status +
                ", result=" + result +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LinkedHashMap<String,Object> getResult() {
        return result;
    }

    public void setResult(LinkedHashMap<String, Object> result) {
        this.result = result;
    }
}
