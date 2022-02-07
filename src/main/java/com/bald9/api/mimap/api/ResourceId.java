package com.bald9.api.mimap.api;

import com.bald9.api.mimap.help.MiRestful;
import com.bald9.utils.url.HttpBody;
import com.bald9.utils.url.Requests;
import com.bald9.utils.url.Response;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.HttpCookie;
import java.util.*;

/**
 * @author bald9
 * @create 2022-02-06 0:44
 */
public class ResourceId {
    private static final String url="https://i.mi.com/passport/lite/device/resourceId";
    public static class Id{
        @Override
        public String toString() {
            return "Id{" +
                    "devId='" + devId + '\'' +
                    ", model='" + model + '\'' +
                    ", modelInfo=" + modelInfo +
                    ", resourceId='" + resourceId + '\'' +
                    '}';
        }

        public String devId;
        public String model;
        public Object modelInfo;
        public String resourceId;
    }
    public static Id get(String devId,String model){
        Properties properties = new Properties();
        List<HttpCookie> serviceTokens = Requests.localCookieStore.get("serviceToken", "i.mi.com");
        if(serviceTokens.size()!=1){
            throw new RuntimeException("多个serviceToken在i.mi.com的异常");
        }
        properties.put("devIds","[{\"devId\":\""+devId+"\",\"model\":\""+model+"\"}]");
        properties.put("serviceToken",serviceTokens.get(0).getValue());
        HttpBody body=new HttpBody("application/x-www-form-urlencoded; charset=UTF-8",properties);
        Response response = Requests.post(url,null,null,body);
        //System.out.println(response.getText());
        MiRestful json = response.getJson(MiRestful.class);

        if(json.getCode()==0){
            LinkedHashMap<String, Object> data =json.getData();
            ArrayList<Object> odevices=(ArrayList<Object>)data.get("list");
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayList<Id> res = objectMapper.convertValue(odevices, new TypeReference<ArrayList<Id>>(){});

            return res.get(0);
        }else{
            throw new RuntimeException("ResourceId接口错误");
        }
    }

    public static void main(String[] args){
        System.setProperty("https.proxyHost", "localhost");
        System.setProperty("https.proxyPort", "8888");
        Id l = ResourceId.get("l3W2YS5ZeIgB_sdl", "21091116C");
        System.out.println(l);
    }
}
