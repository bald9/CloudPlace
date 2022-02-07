package com.bald9.utils.url.Exception;

import com.bald9.utils.url.Response;

/**
 * @author bald9
 * @create 2022-02-04 23:08
 */
public class UnhandleCodeException extends RuntimeException{
    public Response response;
    private static final String massage="无法处理未通过注册Codehandle的响应:";
    public UnhandleCodeException(Response response) {
        super(massage+response.getResponseCode());
    }
}
