package com.zoe.weiya.util;

import com.zoe.weiya.model.ZoeDate;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by chenghui on 2016/12/23.
 */
public class SignLimitUtil {

    private static ZoeDate[] time ={
            new ZoeDate("21","08","")
    };

    public static boolean isEnable(){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int now = c.get(Calendar.HOUR_OF_DAY);
        int day = c.get(Calendar.DATE);
        return true;
    }

}
