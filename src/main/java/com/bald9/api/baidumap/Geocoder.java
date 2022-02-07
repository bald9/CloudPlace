package com.bald9.api.baidumap;

import com.bald9.utils.url.Requests;
import com.bald9.utils.url.Response;
import com.bald9.utils.url.Restful;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

/**
 * @author bald9
 * @create 2022-02-05 0:16
 */
public class Geocoder {
    private static final String url="https://sapi.map.baidu.com/geocoder/v2/";
    private static final String ak ="pcBxGz76Gj5FRx7PLyyu9A7D";//用户申请注册的key，自v2开始参数修改为“ak”，之前版本参数为“key”
    private static final String output="json";//输出格式为json或者xml
    private static final int pois=1;//是否显示指定位置周边的poi，0为不显示，1为显示。当值为1时，显示周边100米内的poi。
    public static Address getPlaceName(String latitude, String longitude){
        Properties header=new Properties();
        Properties query=new Properties();
        query.put("url",url);
        query.put("ak",ak);
        query.put("output",output);
        query.put("location",latitude+","+longitude);//lat<纬度>,lng<经度>
        Response s = Requests.get(url,header,query);
        Restful restfulJson = s.getRestfulJson();
        if(restfulJson.getStatus()==0){
            LinkedHashMap<String, Object> result = restfulJson.getResult();
            String mainAddress=(String)result.get("formatted_address");
            Object res = ((List)result.get("poiRegions")).get(0);
            ObjectMapper objectMapper = new ObjectMapper();
            Address address = objectMapper.convertValue(res, Address.class);
            address.setMain_address(mainAddress);
            return address;
        }else{
            System.out.println(s.getText());
            throw new RuntimeException("Geocoder接口异常");
        }

    }

    public static void main(String[] args) {
        Address address = getPlaceName("30.662866572423387", "114.25938237309993");
        System.out.println("placeName = " + address.getAllPlaceName());
    }
}
