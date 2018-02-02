package com.zoe.weiya.model;

import org.hibernate.validator.constraints.NotBlank;

import java.sql.Timestamp;

/**
 * Created by andy on 2016/12/20.
 */
public class User extends OnlyUser{

    @NotBlank(message = "openId不能为空")
    private String openId;
    @NotBlank(message = "姓名不能为空")
    private String name;
//    @NotNull(message = "是否订餐不能为空")
    private Integer order;
    private String nickName;
    private Integer priceCount;
    private String signFlag;//该时间段内的签到标识符
    private Timestamp signDate = new Timestamp(System.currentTimeMillis());
    public User() {
        super();
    }

    public User(String openId, String signFlag) {
        this.openId = openId;
        this.signFlag = signFlag;
    }

    public Timestamp getSignDate() {
        return signDate;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getPriceCount() {
        return priceCount;
    }

    public void setPriceCount(Integer priceCount) {
        this.priceCount = priceCount;
    }

    public String getSignFlag() {
        return signFlag;
    }

    public void setSignFlag(String signFlag) {
        this.signFlag = signFlag;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
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

    @Override
    public String toString() {
        return "User{" +
                "openId='" + openId + '\'' +
                ", name='" + name + '\'' +
                ", order=" + order +
                ", nickName='" + nickName + '\'' +
                ", priceCount=" + priceCount +
                ", signFlag='" + signFlag + '\'' +
                ", signDate=" + signDate +
                '}';
    }
}
