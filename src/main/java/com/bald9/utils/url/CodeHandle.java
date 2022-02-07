package com.bald9.utils.url;

import java.util.HashMap;
import java.util.function.Function;

public class CodeHandle {
    private HashMap<Integer, Function<RequestInfo, Response>> table = new HashMap<>();

    public CodeHandle() {
    }

    public void add(int begin, int end, Function<RequestInfo, Response> f) {
        for (int i = begin; i <= end; i++) {
            table.put(i, f);
        }
    }
    public void add(int i, Function<RequestInfo, Response> f) {
        table.put(i, f);
    }

    public Function<RequestInfo, Response> getFun(int i) {
        return table.get(i);
    }
}
