package com.bald9.api.mimap.api;

import com.bald9.api.mimap.help.MiRestful;
import com.bald9.api.mimap.UserInfo;
import com.bald9.utils.url.HttpBody;
import com.bald9.utils.url.Requests;
import com.bald9.utils.url.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.HttpCookie;
import java.util.List;
import java.util.Properties;

/**
 * @author bald9
 * @create 2022-02-07 6:19
 */
public class Location {
    private static final String url="https://i.mi.com/find/device/%s/location";//(imei)
    public static class data{
        public String msgId;
        public String syncTag;
        public String version;

        @Override
        public String toString() {
            return "data{" +
                    "msgId='" + msgId + '\'' +
                    ", syncTag='" + syncTag + '\'' +
                    ", version='" + version + '\'' +
                    '}';
        }
    }
    public static data doLocation(UserInfo userInfo){
        String final_url = String.format(url, userInfo.getImei());
        Properties properties = new Properties();
        properties.put("userId",userInfo.getUserId());
        properties.put("imei",userInfo.getImei());
        properties.setProperty("auto","false");
        properties.put("channel","web");
        properties.put("resourceId",userInfo.getResourceId());
        List<HttpCookie> serviceTokens = Requests.localCookieStore.get("serviceToken", "i.mi.com");
        if(serviceTokens.size()!=1){
            throw new RuntimeException("有多个serviceToken在i.mi.com");
        }
        properties.put("serviceToken",serviceTokens.get(0).getValue());
        HttpBody body=new HttpBody("application/x-www-form-urlencoded; charset=UTF-8",properties);
        Response response = Requests.post(final_url,null,null,body);
        MiRestful json = response.getJson(MiRestful.class);
        if(json.getCode()==0){
            if(json.getResult().equals("ok")){
                ObjectMapper objectMapper = new ObjectMapper();
                data data = objectMapper.convertValue(json.getData(), Location.data.class);
                return data;
            }else{
                throw new RuntimeException("未知异常 Location接口错误");
            }

        }else{
            throw new RuntimeException("Location接口错误");
        }
    }
    public static void main(String[] args) {
        data location = Location.doLocation(new UserInfo());
        System.out.println("location = " + location);
    }

}
