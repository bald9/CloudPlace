package com.bald9.utils.url;

import java.net.HttpURLConnection;
import java.util.Properties;

public class RequestInfo {
    public String method;
    public String httpUrl;
    public Properties header;
    public Properties query;
    public HttpBody body;
    public HttpURLConnection connection;
    public int t;

    public RequestInfo(String method, String httpUrl, Properties header, Properties query, HttpBody body, HttpURLConnection connection,int t) {
        this.method = method;
        this.httpUrl = httpUrl;
        this.header = header;
        this.query = query;
        this.body = body;
        this.connection = connection;
        this.t=t;
    }
}
