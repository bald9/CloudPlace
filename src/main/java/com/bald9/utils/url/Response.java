package com.bald9.utils.url;

import com.bald9.utils.url.Exception.JsonToClassFailException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLConnection;

/**
 * @author bald9
 * @create 2022-02-04 20:53
 */
public class Response {
    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    private String res=null;
    private static ObjectMapper mapper=new ObjectMapper();
    private int responseCode;
    private boolean haveDeal;

    public boolean isHaveDeal() {
        return haveDeal;
    }

    public void setHaveDeal(boolean haveDeal) {
        this.haveDeal = haveDeal;
    }

    public URLConnection getConnection() {
        return connection;
    }

    private URLConnection connection;
    static {
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
    }
    public static void setSaveDouble(boolean saveDouble){
        if(!saveDouble){
            mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        }else{
            mapper=new ObjectMapper();
        }
    }
    public String getText(){
        return res;
    }
    public Response(String res, int responseCode, URLConnection connection) {
        this( res,  responseCode,  connection,true);
    }
    public Response(String res, int responseCode, URLConnection connection,boolean haveDeal) {
        this.res=res;
        this.responseCode=responseCode;
        this.connection=connection;
        this.haveDeal=haveDeal;
    }
    public <T> T getJson(Class<T> tClass){
        T t = null;
        try {
            t = mapper.readValue(res, tClass);
        } catch (JsonProcessingException e) {
            throw new JsonToClassFailException(e);
        }
        return t;
    }
    public Object getJson(){
        return getJson(Object.class);
    }
    public Restful getRestfulJson(){
        return getJson(Restful.class);
    }
    @Override
    public String toString() {
        return "Response{" +
                "res='" + res + '\'' +
                '}';
    }
}
