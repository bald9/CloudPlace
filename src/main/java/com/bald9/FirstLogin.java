package com.bald9;

import com.bald9.api.mimap.api.Login;
import com.bald9.utils.url.Requests;

import java.io.*;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * @author bald9
 * @create 2022-02-05 1:38
 */
public class FirstLogin {
    public static void main( String[] args )
    {
        login();

    }

    public static void login(){
        FirstLogin.loadXiaomiCookies();
        Requests.localCookieStore.showCookies();
        Login.loginByXiaomiCookies();
    }
    private static final String path = "src/main/java/resources/XiaomiCookies.cookies";

    public static void loadXiaomiCookies() {
        String cookiesString = null;
        try {
            cookiesString = Files.readAllLines(Paths.get(path)).stream().collect(Collectors.joining());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] split = cookiesString.split(";");
        for (String s : split) {
            addCookie(s.trim());
        }
    }
    public static void addCookie(String cookieString)  {
        String[] split = cookieString.split("=", 2);
        String name=split[0];
        String value=split[1];
        String domain=".account.xiaomi.com";
        if (name.equals("cUserId")||name.equals("uLocale")){
            domain=".xiaomi.com";
        }
        CookieStore cookieStore = Requests.cookieManager.getCookieStore();
        HttpCookie deviceId = new HttpCookie(name, value);
        deviceId.setDomain(domain);
        deviceId.setPath("/");
        cookieStore.add(null, deviceId);
//        try {
//            cookieStore.add(new URI("https://account.xiaomi.com/"), deviceId);
//        } catch (URISyntaxException e) {
//
//            throw new RuntimeException("域名解析失败",e);
//        }
    }
}

