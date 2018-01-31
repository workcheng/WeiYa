package com.zoe.weiya.model;

import java.io.Serializable;

/**
 * Created by andy on 2016/12/18.
 */
public class Area implements Serializable{

    private float latitude ; // 纬度，浮点数，范围为90 ~ -90
    private float longitude; // 经度，浮点数，范围为180 ~ -180。
    private String speed; // 速度，以米/每秒计
    private String accuracy; // 位置精度

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    @Override
    public String toString() {
        return "Area{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", speed='" + speed + '\'' +
                ", accuracy='" + accuracy + '\'' +
                '}';
    }
}
