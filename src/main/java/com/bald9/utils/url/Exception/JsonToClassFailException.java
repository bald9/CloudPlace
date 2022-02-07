package com.bald9.utils.url.Exception;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author bald9
 * @create 2022-02-04 23:08
 */
public class JsonToClassFailException extends RuntimeException{

    private static final String massage="无法将json数据转为给定类(检查给定类是否和json匹配)";
    public JsonToClassFailException(JsonProcessingException e) {
        super(massage,e);
    }
}
