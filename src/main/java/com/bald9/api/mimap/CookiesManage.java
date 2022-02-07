package com.bald9.api.mimap;

import com.bald9.api.mimap.api.Login;
import com.bald9.utils.url.Exception.JsonToClassFailException;
import com.bald9.utils.url.RequestInfo;
import com.bald9.utils.url.Requests;
import com.bald9.utils.url.Response;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author bald9
 * @create 2022-02-07 19:37
 */
public class CookiesManage {
    public static class ErrorRestful{
        public int R;
        public String S;
        public String D;
        public boolean isSecondValidation;

    }
    public static Response expireDeal(RequestInfo requestInfo) {
        InputStream conInputStream = requestInfo.connection.getErrorStream();
        String result = null;
        Response response = null;

        try {
            result = Requests.getAllData(conInputStream);
            response = new Response(result, requestInfo.connection.getResponseCode(), requestInfo.connection, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ErrorRestful json =null;
        try {
            json = response.getJson(ErrorRestful.class);
        } catch (JsonToClassFailException e) {
            return response;
        }
        if(json.R==401||json.S.equals("Err")){
            System.out.println("出现cookies过期的401");
            Requests.showAllInfo(requestInfo.connection,result);
            Login.loginByXiaomiCookies();
            return Requests.request(requestInfo.method,requestInfo.httpUrl,requestInfo.header, requestInfo.query, requestInfo.body,requestInfo.t+1);
        }else{
            System.out.println("出现401,并且带响应内容并且值是未知的");
            Requests.showAllInfo(requestInfo.connection,result);
            throw new RuntimeException("出现401,并且带响应内容并且值是未知的");
        }
    }
}
