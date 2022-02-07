package com.bald9.api.mimap;


import com.bald9.api.mimap.api.Iplocale;
import com.bald9.api.mimap.api.Status;
import com.bald9.api.mimap.api.ResourceId;
import com.bald9.api.mimap.api.UserId;

/**
 * @author bald9
 * @create 2022-02-05 16:51
 */
public class UserInfo {
    private String ipLocale="zh_CN";
    private String userId="2613094053";
    private String imei="2d695e44595b2a7c2f4673636c20793146545c6b6e3566337a774a5b31302733_6c493e83f7f90a5c0fe4b2a24cbe591185349b4bad744613b9b62a3b";
    private String resourceId="V2_0153d0c2_8b20_42c9_8b9b_bc336d186447";
    private Status.Device device;
    private ResourceId.Id ids;


    public UserInfo(){
        loadLatestInfo();
    }
    public static void main(String[] args) {
        System.setProperty("https.proxyHost", "localhost");
        System.setProperty("https.proxyPort", "8888");
        UserInfo userInfo = new UserInfo();

    }
    public void loadLatestInfo(){
        ipLocale= Iplocale.get();
        userId= UserId.get();
        device = Status.getDevices().get(0);
        imei = device.imei;
        ids =ResourceId.get(device.devId,device.model);
        resourceId=ids.resourceId;
    }

    public String getIpLocale() {
        return ipLocale;
    }

    public void setIpLocale(String ipLocale) {
        this.ipLocale = ipLocale;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Status.Device getDevice() {
        return device;
    }

    public void setDevice(Status.Device device) {
        this.device = device;
    }

    public ResourceId.Id getIds() {
        return ids;
    }

    public void setIds(ResourceId.Id ids) {
        this.ids = ids;
    }
}
