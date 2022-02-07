package com.bald9.utils.url;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Properties;

/**
 * @author bald9
 * @create 2022-02-03 0:00
 */
public class HttpBody {
    private String body;
    private String contentType;
    public HttpBody(String contentType, Object body) {
        this(contentType,body,false);

    }
    public HttpBody(String contentType, Object body,boolean force) {
        if(force){
            this.contentType=contentType;
            this.body=body.toString();
            return;
        }
        this.contentType=contentType;
        if(contentType.contains("application/x-www-form-urlencoded")){
            this.body=FormUrlencoded((Properties)body);
        }
        else{
            this.body=body.toString();
            System.out.println("未知的HttpBody类型");
        }
    }
    private String FormUrlencoded(Properties body){
        StringBuffer temp=new StringBuffer();
        boolean a=false;
        for (String key:body.stringPropertyNames()) {
            if(!a){
                a=true;
            }else{
                temp.append('&');
            }
            temp.append(key);
            temp.append('=');
            try {
                temp.append(URLEncoder.encode(body.getProperty(key),"UTF-8") );
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return temp.toString();
    }

    public String toString() {
        return body;
    }

    public String getType(){
        return contentType;
    }

}
