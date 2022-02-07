package com.bald9.api.mimap.api;

import com.bald9.api.mimap.help.MiRestful;
import com.bald9.utils.url.Requests;
import com.bald9.utils.url.Response;

import java.util.LinkedHashMap;
import java.util.Properties;

/**
 * @author bald9
 * @create 2022-02-05 3:12
 */
public class Login {
    private static final String url="https://i.mi.com/api/user/login";
    private static final String followUp="https://i.mi.com/mobile/find";
    private static final String _locale="zh_CN";
    public static void loginByXiaomiCookies(){
        Properties header=new Properties();
        Properties query=new Properties();
        query.put("followUp",followUp);
        query.put("_locale",_locale);
        query.put("ts",System.currentTimeMillis());
        Response s = Requests.get(url,header,query);
        MiRestful miRestful = s.getJson(MiRestful.class);
        System.out.println(s);
        if(miRestful.getCode()==0){
            LinkedHashMap<String, Object> data = miRestful.getData();
            String loginUrl =(String) data.get("loginUrl");
            Response response = Requests.get(loginUrl);
            if(response.getResponseCode()==200){
                System.out.println(response);
                return ;
            }else{
                System.out.println(response.getText());
                throw new RuntimeException("302跳转时出错");
            }
        }else{
            System.out.println(s.getText());
            throw new RuntimeException("login接口异常");
        }
    }
}
