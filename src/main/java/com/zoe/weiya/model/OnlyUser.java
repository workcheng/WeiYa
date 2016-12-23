package com.zoe.weiya.model;

import java.io.Serializable;

/**
 * Created by chenghui on 2016/12/22.
 */
public class OnlyUser implements Serializable {

    private String openId;
    private String name;
    private String depName;
    private Integer order;
    private String headImgUrl;
    private String signFlag;
    private boolean isLucky;

    public OnlyUser() {

    }


//    public OnlyUser(String openId, String signFlag) {
//        this.openId = openId;
//        this.signFlag = signFlag;
//    }


    public OnlyUser(String openId, String signFlag) {
        this.openId = openId;
        this.signFlag = signFlag;
    }

    public boolean isLucky() {
        return isLucky;
    }

    public void setLucky(boolean lucky) {
        isLucky = lucky;
    }

    public String getSignFlag() {
        return signFlag;
    }

    public void setSignFlag(String signFlag) {
        this.signFlag = signFlag;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }



}
