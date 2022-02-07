package com.bald9.utils.url.Exception;

/**
 * @author bald9
 * @create 2022-02-06 0:29
 */
public class MaxRequestException extends RuntimeException {
    private static final String massage="达到最大Requsets.request调用次数";
    public MaxRequestException(Exception e) {
        super(massage,e);
    }
    public MaxRequestException() {
        super(massage);
    }
}
