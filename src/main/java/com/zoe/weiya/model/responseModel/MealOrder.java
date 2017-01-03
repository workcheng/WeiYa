package com.zoe.weiya.model.responseModel;

import com.zoe.weiya.model.OnlyUser;
import com.zoe.weiya.model.ZoeDate;

import java.util.List;

/**
 * Created by chenghui on 2017/1/3.
 */
public class MealOrder {

    private int orderCount;
    private List<OnlyUser> orderUsers;
    private ZoeDate now;

    public ZoeDate getNow() {
        return now;
    }

    public void setNow(ZoeDate now) {
        this.now = now;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public List<OnlyUser> getOrderUsers() {
        return orderUsers;
    }

    public void setOrderUsers(List<OnlyUser> orderUsers) {
        this.orderUsers = orderUsers;
    }

    @Override
    public String toString() {
        return "MealOrder{" +
                "orderCount=" + orderCount +
                ", orderUsers=" + orderUsers +
                '}';
    }
}
