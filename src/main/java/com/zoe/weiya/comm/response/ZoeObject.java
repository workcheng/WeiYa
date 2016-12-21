package com.zoe.weiya.comm.response;

import com.zoe.weiya.comm.constant.ZoeErrorCode;
import me.chanjar.weixin.common.bean.result.WxError;
import me.chanjar.weixin.common.exception.WxErrorException;

/**
 * Created by andy on 2016/12/17.
 */
public class ZoeObject {

    public static ResponseMsg success(){
        ResponseMsg msg = new ResponseMsg();
        msg.setData("");
        msg.setMessage(ZoeErrorCode.SUCCESS.getDescription());
        msg.setStatus(ZoeErrorCode.SUCCESS.getCode());
        return msg;
    }

    public static ResponseMsg success(Object obj){
        ResponseMsg msg = new ResponseMsg();
        msg.setData(obj);
        msg.setMessage(ZoeErrorCode.SUCCESS.getDescription());
        msg.setStatus(ZoeErrorCode.SUCCESS.getCode());
        return msg;
    }

    public static ResponseMsg failure(){
        ResponseMsg msg = new ResponseMsg();
        msg.setData("");
        msg.setMessage(ZoeErrorCode.ERROR.getDescription());
        msg.setStatus(ZoeErrorCode.ERROR.getCode());
        return msg;
    }

    public static ResponseMsg failure(Object obj){
        ResponseMsg msg = new ResponseMsg();
        msg.setData(obj);
        msg.setMessage(ZoeErrorCode.ERROR.getDescription());
        msg.setStatus(ZoeErrorCode.ERROR.getCode());
        return msg;
    }

    public static ResponseMsg failure(Enum enu){
        ZoeErrorCode enu1 = (ZoeErrorCode) enu;
        ResponseMsg msg = new ResponseMsg();
        msg.setData("");
        msg.setMessage(enu1.getDescription());
        msg.setStatus(enu1.getCode());
        return msg;
    }

    public static ResponseMsg failure(WxErrorException e){
        ResponseMsg msg = new ResponseMsg();
        if(null != e){
            WxError error = e.getError();
            if(null != error){
                msg.setData("");
                msg.setMessage(error.getErrorMsg());
                msg.setStatus(error.getErrorCode());
                return msg;
            }else{
                return ZoeObject.failure(ZoeErrorCode.ERROR_INTERNAL);
            }
        }else{
            return ZoeObject.failure(ZoeErrorCode.ERROR_INTERNAL);
        }
    }
}
