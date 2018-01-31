package com.zoe.weiya.model.responseModel;

import com.zoe.weiya.model.User;
import com.zoe.weiya.model.ZoeDate;

import java.util.List;

/**
 * Created by chenghui on 2017/1/9.
 */
public class UserListCount {

    private int orderCount;
    private List<User> users;
    private ZoeDate now;

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public ZoeDate getNow() {
        return now;
    }

    public void setNow(ZoeDate now) {
        this.now = now;
    }

    @Override
    public String toString() {
        return "UserListCount{" +
                "orderCount=" + orderCount +
                ", users=" + users +
                ", now=" + now +
                '}';
    }
}
