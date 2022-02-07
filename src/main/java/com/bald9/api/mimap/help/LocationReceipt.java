package com.bald9.api.mimap.help;

/**
 * @author bald9
 * @create 2022-02-08 0:02
 */
public class LocationReceipt {
    public Object gpsInfo;
    public Object gpsInfoExtra;
    public String infoTime;
    public String msgId;
    public String phone;
    public int powerLevel;
    public String responseType;
    public String serverReceiveTime;
    public String tag;
    public String type;

    @Override
    public String toString() {
        return "LocationReceipt{" +
                "gpsInfo=" + gpsInfo +
                ", gpsInfoExtra=" + gpsInfoExtra +
                ", infoTime='" + infoTime + '\'' +
                ", msgId='" + msgId + '\'' +
                ", phone='" + phone + '\'' +
                ", powerLevel=" + powerLevel +
                ", responseType='" + responseType + '\'' +
                ", serverReceiveTime='" + serverReceiveTime + '\'' +
                ", tag='" + tag + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
