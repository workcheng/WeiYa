package com.zoe.weiya.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by chenghui on 2017/1/16.
 */
public class LuckyUserMessage implements Serializable {
    private String openId;
    private String name;
    private Integer degree;
    private Timestamp signDate;
    private String depName;
    private Timestamp hitTime = new Timestamp(System.currentTimeMillis());
    private String message;
    private String nickName;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDegree() {
        return degree;
    }

    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    public Timestamp getSignDate() {
        return signDate;
    }

    public void setSignDate(Timestamp signDate) {
        this.signDate = signDate;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public Timestamp getHitTime() {
        return hitTime;
    }

    public void setHitTime(Timestamp hitTime) {
        this.hitTime = hitTime;
    }

    @Override
    public String toString() {
        return "LuckyUserMessage{" +
                "openId='" + openId + '\'' +
                ", name='" + name + '\'' +
                ", degree=" + degree +
                ", signDate=" + signDate +
                ", depName='" + depName + '\'' +
                ", hitTime=" + hitTime +
                ", message='" + message + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
