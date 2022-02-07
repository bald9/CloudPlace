package com.bald9.api.mimap.api;

import com.bald9.api.mimap.help.MiRestful;
import com.bald9.utils.url.Exception.UnhandleCodeException;
import com.bald9.utils.url.Requests;
import com.bald9.utils.url.Response;

import java.nio.file.Paths;
import java.util.HashMap;

/**
 * @author bald9
 * @create 2022-02-06 0:43
 */
public class UserId {
    private static final String url="https://i.mi.com/status/lite/profile";

    public static String get(){
        Response response = Requests.getWithTs(url);
        MiRestful json = response.getJson(MiRestful.class);
        //System.out.println(response.getText());
        if(json.getCode()==0){
            return (String) ((HashMap<String,Object>)json.getData()).get("nickname");
        }else{
            throw new RuntimeException("UserId接口错误");
        }
    }

    public static void main(String[] args) {
        Requests.localCookieStore.setStorePath(Paths.get("data/CookiesStore_old.cookies"));
        Requests.localCookieStore.showCookies();
        try {
            String s = UserId.get();
            System.out.println(s);
        }catch (UnhandleCodeException e){
            e.printStackTrace();
        }
        Requests.localCookieStore.showCookies();


    }
}
