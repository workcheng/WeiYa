package com.zoe.weiya.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by chenghui on 2017/1/5.
 */
public class LuckyUser {
    @NotBlank
    private String openId;
    @NotBlank
    private String name;
    @NotNull
    private Integer degree;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getDegree() {
        return degree;
    }

    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    @Override
    public String toString() {
        return "LuckyUser{" +
                "openId='" + openId + '\'' +
                ", degree=" + degree +
                '}';
    }
}
