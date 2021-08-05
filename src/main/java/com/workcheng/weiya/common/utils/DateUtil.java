package com.workcheng.weiya.common.utils;

import com.workcheng.weiya.common.constant.CommonConstant;
import com.workcheng.weiya.common.constant.ErrorCode;
import com.workcheng.weiya.common.exception.NotStartException;
import com.workcheng.weiya.common.dto.MyDate;
import com.workcheng.weiya.common.config.WeiYaConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenghui on 2017/1/4.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DateUtil {
    private final WeiYaConfig weiYaConfig;

    public String getIndex() throws NotStartException {
        //TODO 考虑跨年跨月的情况,目前只支持三天，活动当天上下午和晚上，获得第二天第三天上下午和晚上
        MyDate now = MyDateUtil.moment();
        MyDate startTime = PropertiesUtil.getStartTime(weiYaConfig);
        log.info("now:{}, startTime:{}", now, startTime);
        if (!now.getYear().equals(startTime.getYear())) {
            throw new NotStartException(ErrorCode.NOT_START.getDescription());
        }
        if (now.getMonth() < startTime.getMonth()) {
            throw new NotStartException(ErrorCode.NOT_START.getDescription());
        }
        if (now.getDay() < startTime.getDay()) {
            throw new NotStartException(ErrorCode.NOT_START.getDescription());
        }
        if (CommonConstant.FIRST_DAY.equals(MyDateUtil.whichDay(now, weiYaConfig))) {
            switch (MyDateUtil.getTime(now, weiYaConfig)) {
                case CommonConstant.MORNING:
                    return CommonConstant.SIGN_USER_FIRST_DAY_MORNING;
                case CommonConstant.NOON:
                    return CommonConstant.SIGN_USER_FIRST_DAY_NOON;
                default:
                    throw new NotStartException(ErrorCode.NOT_START.getDescription());
            }
        } else if (CommonConstant.SECOND_DAY.equals(MyDateUtil.whichDay(now, weiYaConfig))) {
            switch (MyDateUtil.getTime(now, weiYaConfig)) {
                case CommonConstant.MORNING:
                    return CommonConstant.SIGN_USER_SECOND_DAY_MORNING;
                case CommonConstant.NOON:
                    return CommonConstant.SIGN_USER_SECOND_DAY_NOON;
                case CommonConstant.NIGHT:
                    return CommonConstant.SIGN_USER_SECOND_DAY_NIGHT;
                default:
                    throw new NotStartException(ErrorCode.NOT_START.getDescription());
            }
        }
        throw new NotStartException(ErrorCode.NOT_START.getDescription());
    }

    public String getSetIndex() throws NotStartException {
        return CommonConstant.SET + getIndex();
    }

    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
