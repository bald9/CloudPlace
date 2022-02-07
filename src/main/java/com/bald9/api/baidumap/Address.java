package com.bald9.api.baidumap;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Address {
    @JsonIgnore
    private String main_address;
    private String direction_desc;
    private String name;
    private String tag;
    private String uid;
    private String distance;

    public String getMain_address() {
        return main_address;
    }

    public void setMain_address(String main_address) {
        this.main_address = main_address;
    }

    @Override
    public String toString() {
        return "PoiRegions{" +
                "direction_desc='" + direction_desc + '\'' +
                ", name='" + name + '\'' +
                ", tag='" + tag + '\'' +
                ", uid='" + uid + '\'' +
                ", distance='" + distance + '\'' +
                '}';
    }

    public String getPlaceName() {
        return main_address+name + tag + direction_desc;
    }

    public String getAllPlaceName() {
        return getPlaceName() + " 距离：" + distance;
    }

    public String getDirection_desc() {
        return direction_desc;
    }

    public void setDirection_desc(String direction_desc) {
        this.direction_desc = direction_desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
