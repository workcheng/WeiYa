package com.zoe.weiya.comm.response;

/**
 * Created by andy on 2016/12/17.
 */
public class ResponseMsg {
    private Object data;
    private String message;
    private String status;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStatus(int status) {
        this.status = String.valueOf(status);
    }

    @Override
    public String toString() {
        return "ResponseMsg{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
