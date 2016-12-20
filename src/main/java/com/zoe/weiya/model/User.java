package com.zoe.weiya.model;

import me.chanjar.weixin.mp.bean.result.WxMpUser;

import java.io.Serializable;

/**
 * Created by andy on 2016/12/20.
 */
public class User extends WxMpUser implements Serializable {

    private String openid;
    private String name;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "openid='" + openid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
