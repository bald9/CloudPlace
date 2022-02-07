package com.bald9.api.mimap.help;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

/**
 * @author bald9
 * @create 2022-02-05 3:22
 */
public class MiRestful {
    private String result;
    private boolean retriable;
    private int code;
    private String description;
    private LinkedHashMap<String,Object> data;
    private BigDecimal ts;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isRetriable() {
        return retriable;
    }

    public void setRetriable(boolean retriable) {
        this.retriable = retriable;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LinkedHashMap<String, Object> getData() {
        return data;
    }

    public void setData(LinkedHashMap<String, Object> data) {
        this.data = data;
    }

    public BigDecimal getTs() {
        return ts;
    }

    public void setTs(BigDecimal ts) {
        this.ts = ts;
    }
}
