package com.zoe.weiya.util;

import com.zoe.weiya.comm.constant.CommonConstant;
import com.zoe.weiya.comm.constant.ZoeErrorCode;
import com.zoe.weiya.comm.exception.NotStartException;
import com.zoe.weiya.comm.properties.ZoeProperties;
import com.zoe.weiya.model.ZoeDate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenghui on 2017/1/4.
 */
public class ZoeUtil {

    public static String getIndex() throws NotStartException {
        //TODO 考虑跨年跨月的情况,目前只支持三天，活动当天上下午和晚上，获得第二天第三天上下午和晚上
        ZoeDate now = ZoeDateUtil.moment();
        ZoeDate startTime = ZoeProperties.getStartTime();
        if(now.getYear().equals(startTime.getYear())){
            if(now.getMonth() >= startTime.getMonth()){
                if(now.getDay() >= startTime.getDay()){
                    if(CommonConstant.FIRST_DAY.equals(ZoeDateUtil.whichDay(now))){
                        switch (ZoeDateUtil.getTime(now)){
                            case CommonConstant.MORNING:
                                return CommonConstant.SIGN_USER_FIRST_DAY_MORNING;
                            case CommonConstant.NOON:
                                return CommonConstant.SIGN_USER_FIRST_DAY_NOON;
                            case CommonConstant.NIGHT:
                                return CommonConstant.SIGN_USER_FIRST_DAY_NIGHT;
                            default:
                                throw new NotStartException(ZoeErrorCode.NOT_START.getDescription());
                        }
                    }else if(CommonConstant.SECOND_DAY.equals(ZoeDateUtil.whichDay(now))){
                        switch (ZoeDateUtil.getTime(now)){
                            case CommonConstant.MORNING:
                                return CommonConstant.SIGN_USER_SECOND_DAY_MORNING;
                            case CommonConstant.NOON:
                                return CommonConstant.SIGN_USER_SECOND_DAY_NOON;
                            case CommonConstant.NIGHT:
                                return CommonConstant.SIGN_USER_SECOND_DAY_NIGHT;
                            default:
                                throw new NotStartException(ZoeErrorCode.NOT_START.getDescription());
                        }
                    }else if(CommonConstant.THIRD_DAY.equals(ZoeDateUtil.whichDay(now))){
                        switch (ZoeDateUtil.getTime(now)){
                            case CommonConstant.MORNING:
                                return CommonConstant.SIGN_USER_THIRD_DAY_MORNING;
                            case CommonConstant.NOON:
                                return CommonConstant.SIGN_USER_THIRD_DAY_NOON;
                            case CommonConstant.NIGHT:
                                return CommonConstant.SIGN_USER_THIRD_DAY_NIGHT;
                            default:
                                throw new NotStartException(ZoeErrorCode.NOT_START.getDescription());
                        }
                    }
                    throw new NotStartException(ZoeErrorCode.NOT_START.getDescription());

                }
            }
        }
        throw new NotStartException(ZoeErrorCode.NOT_START.getDescription());
    }

    public String getSetIndex() throws NotStartException {
        return CommonConstant.SET+getIndex();
    }

    public static HttpServletRequest getHttpServletRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
