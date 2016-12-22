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
    private Integer priceCount;
    private String signFlag;//该时间段内的签到标识符

    public String getSignFlag() {
        return signFlag;
    }

    public void setSignFlag(String signFlag) {
        this.signFlag = signFlag;
    }

    public Integer getPriceCount() {
        return priceCount;
    }

    public void setPriceCount(Integer priceCount) {
        this.priceCount = priceCount;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OnlyUser)) return false;

        OnlyUser onlyUser = (OnlyUser) o;

        if (!openId.equals(onlyUser.openId)) return false;
        return signFlag.equals(onlyUser.signFlag);

    }

    @Override
    public int hashCode() {
        int result = openId.hashCode();
        result = 31 * result + signFlag.hashCode();
        return result;
    }
}
