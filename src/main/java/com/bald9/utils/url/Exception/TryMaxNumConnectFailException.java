package com.bald9.utils.url.Exception;

import java.net.SocketTimeoutException;

/**
 * @author bald9
 * @create 2022-02-04 23:35
 */
public class TryMaxNumConnectFailException extends RuntimeException{
    private static final String massage="达到尝试次数无法连接该host";
    public TryMaxNumConnectFailException(SocketTimeoutException e) {
        super(massage,e);
    }
}
