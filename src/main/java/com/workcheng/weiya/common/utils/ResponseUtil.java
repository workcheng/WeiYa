package com.workcheng.weiya.common.utils;


import com.workcheng.weiya.common.constant.ErrorCode;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;

/**
 * Created by andy on 2016/12/17.
 */
public class ResponseUtil {

    public static ResponseMsg success() {
        ResponseMsg msg = new ResponseMsg();
        msg.setData("");
        msg.setMessage(ErrorCode.SUCCESS.getDescription());
        msg.setStatus(ErrorCode.SUCCESS.getCode());
        return msg;
    }

    public static ResponseMsg success(Object obj) {
        ResponseMsg msg = new ResponseMsg();
        msg.setData(obj);
        msg.setMessage(ErrorCode.SUCCESS.getDescription());
        msg.setStatus(ErrorCode.SUCCESS.getCode());
        return msg;
    }

    public static ResponseMsg failure() {
        ResponseMsg msg = new ResponseMsg();
        msg.setData("");
        msg.setMessage(ErrorCode.ERROR.getDescription());
        msg.setStatus(ErrorCode.ERROR.getCode());
        return msg;
    }

    public static ResponseMsg failure(Object obj) {
        ResponseMsg msg = new ResponseMsg();
        msg.setData(obj);
        msg.setMessage(ErrorCode.ERROR.getDescription());
        msg.setStatus(ErrorCode.ERROR.getCode());
        return msg;
    }

    public static ResponseMsg failure(Enum enu) {
        return failure(enu, "");
    }

    public static ResponseMsg failure(Enum enu, String data) {
        ErrorCode enu1 = (ErrorCode) enu;
        ResponseMsg msg = new ResponseMsg();
        msg.setData(data);
        msg.setMessage(enu1.getDescription());
        msg.setStatus(enu1.getCode());
        return msg;
    }

    public static ResponseMsg failure(WxErrorException e) {
        ResponseMsg msg = new ResponseMsg();
        if (null != e) {
            WxError error = e.getError();
            if (null != error) {
                msg.setData("");
                msg.setMessage(error.getErrorMsg());
                msg.setStatus(error.getErrorCode());
                return msg;
            } else {
                return ResponseUtil.failure(ErrorCode.ERROR_INTERNAL);
            }
        } else {
            return ResponseUtil.failure(ErrorCode.ERROR_INTERNAL);
        }
    }
}
