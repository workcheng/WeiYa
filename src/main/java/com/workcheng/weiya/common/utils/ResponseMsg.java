package com.workcheng.weiya.common.utils;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author andy
 * @date 2016/12/17
 */
@Data
public class ResponseMsg implements Serializable {
    private Object data;
    private String message;
    private Integer status;
    private Object extraData;

    @Override
    public String toString() {
        return "ResponseMsg{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
