package com.zoe.weiya.comm.Respon;

/**
 * Created by andy on 2016/12/17.
 */
public class ZoeObject {

    public static ResponseMsg success(Object obj){
        ResponseMsg msg = new ResponseMsg();
        msg.setData(obj);
        msg.setMessage("success");
        msg.setStatus("1");
        return msg;
    }

    public static ResponseMsg failure(Object obj){
        ResponseMsg msg = new ResponseMsg();
        msg.setData(obj);
        msg.setMessage("fail");
        msg.setStatus("0");
        return msg;
    }
}
