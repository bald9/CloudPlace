package com.bald9.utils.url;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.HttpCookie;
import java.util.Objects;
import java.util.TimeZone;

/**
 * @author bald9
 * @create 2022-02-05 5:29
 */
public class CookieSave implements Serializable {
    private String name;  // NAME= ... "$Name" style is reserved
    private String value;       // value of NAME

    // Attributes encoded in the header's cookie fields.
    private String comment;     // Comment=VALUE ... describes cookie's use
    private String commentURL;  // CommentURL="http URL" ... describes cookie's use
    private boolean toDiscard;  // Discard ... discard cookie unconditionally
    private String domain;      // Domain=VALUE ... domain that sees cookie
    private String path;        // Path=VALUE ... URLs that see the cookie
    private String portlist;    // Port[="portlist"] ... the port cookie may be returned to
    private boolean secure;     // Secure ... e.g. use SSL
    private boolean httpOnly;   // HttpOnly ... i.e. not accessible to scripts
    private int version = 1;    // Version=1 ... RFC 2965 style

    // The original header this cookie was consructed from, if it was
    // constructed by parsing a header, otherwise null.
    private String header;

    //private final long whenCreated;

    //static TimeZone GMT ;

    private <T> T getPrf(String fieldName,HttpCookie hc){
        Class<HttpCookie> httpCookieClass = HttpCookie.class;
        try {
            Field field = httpCookieClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object o = field.get(hc);
            return (T)o;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private <T> boolean setPrf(String fieldName,HttpCookie hc,T value){
        Class<HttpCookie> httpCookieClass = HttpCookie.class;
        try {
            Field field = httpCookieClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(hc,value);
            return true;
        } catch (NoSuchFieldException  e) {
            e.printStackTrace();
        }catch (IllegalAccessException e1){
            e1.printStackTrace();
            return false;
        }
        return false;
    }

    public CookieSave(HttpCookie hc){
        name=hc.getName();
        domain = hc.getDomain();
         path = hc.getPath();
         comment = hc.getComment();
        toDiscard = hc.getDiscard();
         commentURL = hc.getCommentURL();
         portlist = hc.getPortlist();
         secure = hc.getSecure();
         value = hc.getValue();
         httpOnly = hc.isHttpOnly();
         version = hc.getVersion();
         header=this.<String>getPrf("header",hc);
        //GMT=this.<TimeZone>getPrf("GMT",hc);
    }
    public HttpCookie toHttpCookie(){
        HttpCookie hc=new HttpCookie(name,value);
        hc.setDomain(domain);
        hc.setPath(path);
        hc.setComment(comment);
        hc.setDiscard(toDiscard);
        hc.setCommentURL(commentURL);
        hc.setPortlist(portlist);
        hc.setSecure(secure);
        hc.setHttpOnly(httpOnly);
        hc.setVersion(version);
        this.<String>setPrf("header",hc,header);
        //this.<TimeZone>setPrf("GMT",hc,GMT);

        return hc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CookieSave that = (CookieSave) o;
        return name.equals(that.name) && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
