package com.bald9.utils.url;

import com.bald9.utils.url.CookieSave;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CookiesSave implements Serializable {
    private HashMap<String, ArrayList<CookieSave>> cookieSave=new HashMap<>();

    public HashMap<String, ArrayList<CookieSave>> getCookieSave() {
        return cookieSave;
    }

    public void setCookieSave(HashMap<String, ArrayList<CookieSave>> cookieSave) {
        this.cookieSave = cookieSave;
    }

    public CookiesSave(){
    }

    public CookiesSave(CookieStore cookieStore){
        saveFromCookieStore(cookieStore);
    }

    public void add(String uri,CookieSave cookie){
        ArrayList<CookieSave> oldList ;
        if((oldList=cookieSave.getOrDefault(uri,null))!=null){
            oldList.add(cookie);
        }else{
            ArrayList<CookieSave> c = new ArrayList<>();
            c.add(cookie);
            cookieSave.put(uri, c);
        }
    }

    public void saveFromCookieStore_old(CookieStore cookieStore){
        List<URI> urIs = cookieStore.getURIs();
        for (URI is : urIs) {
            List<HttpCookie> httpCookies = cookieStore.get(is);
            for (HttpCookie cookie : httpCookies) {
                add(is.toString(),new CookieSave(cookie));
            }
        }
    }
    public void saveFromCookieStore(CookieStore cookieStore){
        List<HttpCookie> allCookies = cookieStore.getCookies();
        allCookies =new ArrayList<>(allCookies);
        List<URI> urIs = cookieStore.getURIs();
        for (URI is : urIs) {
            List<HttpCookie> httpCookies = cookieStore.get(is);
            for (HttpCookie cookie : httpCookies) {
                add(is.toString(),new CookieSave(cookie));
                if(allCookies.contains(cookie)){
                    allCookies.remove(cookie);
                }else{
                    System.out.println("cookie理论出错");
                }
            }
        }
        for (HttpCookie hc : allCookies) {
            add(null,new CookieSave(hc));
        }

    }

    public void backToCookieStore_old(CookieStore cookieStore){
        for (String uri : cookieSave.keySet()) {
            for (CookieSave cs : cookieSave.get(uri)) {
                try {
                    cookieStore.add(new URI(uri),cs.toHttpCookie());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void backToCookieStore(CookieStore cookieStore){
        if(cookieStore==null) return ;
        for (String uri : cookieSave.keySet()) {
            for (CookieSave cs : cookieSave.get(uri)) {
                try {
                    if(uri==null){
                        cookieStore.add(null, cs.toHttpCookie());
                    }else {
                        cookieStore.add(new URI(uri), cs.toHttpCookie());
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
