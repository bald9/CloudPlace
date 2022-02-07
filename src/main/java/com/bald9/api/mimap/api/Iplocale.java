package com.bald9.api.mimap.api;

import com.bald9.api.mimap.help.MiRestful;
import com.bald9.utils.url.Requests;
import com.bald9.utils.url.Response;

import java.util.HashMap;

/**
 * @author bald9
 * @create 2022-02-05 17:03
 */
public class Iplocale {
    private static final String url="https://i.mi.com/api/iplocale";

    public static String get(){
        Response response = Requests.getWithTs(url);
        MiRestful json = response.getJson(MiRestful.class);
        //System.out.println(response.getText());
        if(json.getCode()==0){
            return (String) ((HashMap<String,Object>)json.getData()).get("ipLocale");
        }else{
            throw new RuntimeException("Iplocale接口错误");
        }
    }
}
