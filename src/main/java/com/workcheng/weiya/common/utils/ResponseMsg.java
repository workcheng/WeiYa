package com.workcheng.weiya.common.utils;

import lombok.Data;

/**
 * Created by andy on 2016/12/17.
 */
@Data
public class ResponseMsg {
    private Object data;
    private String message;
    private Integer status;

    @Override
    public String toString() {
        return "ResponseMsg{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
