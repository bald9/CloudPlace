package com.bald9.api.mimap.api;

import com.bald9.api.mimap.help.MiRestful;
import com.bald9.api.mimap.help.LocationReceipt;
import com.bald9.utils.url.Requests;
import com.bald9.utils.url.Response;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author bald9
 * @create 2022-02-06 0:44
 */
public class Status {
    private static final String url="https://i.mi.com/find/device/full/status";

    public static Object get(){
        Response response = Requests.getWithTs(url);
        MiRestful json = response.getJson(MiRestful.class);
        //System.out.println(response.getText());
        if(json.getCode()==0){
            return json.getData();
        }else{
            throw new RuntimeException("Status接口错误");
        }
    }
    public static class Device{
        public Object commandList;
        public String devId;
        public String deviceType;
        public String imei;
        public String isTZDevice;
        public Object lastLocationReceipt;
        public Object lastResponse;
        public Object locationReceiptList;
        public String model;
        public String phone;
        public String regId;
        public int snapshot;
        public String status;
        public long updateTime;
        public String version;
    }
    public static ArrayList<Device> getDevices(){
        HashMap<String,Object> o = (HashMap<String,Object>)get();
        ArrayList<Object> odevices=(ArrayList<Object>)o.get("devices");
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Device> devices = objectMapper.convertValue(odevices, new TypeReference<ArrayList<Device>>(){});
        return devices;
    }
    public static LocationReceipt getLastLocationReceipt(){
        ArrayList<Device> o = getDevices();
        if(o.size()!=1){
            System.out.println("多设备问题");
            throw new RuntimeException("多设备异常");
        }
        Device device = o.get(0);

        ObjectMapper objectMapper = new ObjectMapper();
        LocationReceipt devices = objectMapper.convertValue(device.lastLocationReceipt, LocationReceipt.class);
        return devices;
    }

    public static void main(String[] args) {
        LocationReceipt lastLocationReceipt = getLastLocationReceipt();
        System.out.println("lastLocationReceipt = " + lastLocationReceipt);
    }
}
