package com.zoe.weiya.model;

import java.io.Serializable;

/**
 * Created by chenghui on 2017/1/10.
 */
public class Message extends LuckyUser implements Serializable {
    private String message;
    private ZoeDate createTime;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ZoeDate getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZoeDate createTime) {
        this.createTime = createTime;
    }
}
