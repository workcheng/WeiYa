package com.zoe.weiya.util;

import com.zoe.weiya.model.ZoeDate;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by chenghui on 2016/12/28.
 */
public class ZoeDateUtil {

    private static Calendar now(){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return c;
    }

    public static ZoeDate moment(){
        Calendar c = now();
        int day = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int second = c.get(Calendar.SECOND);
        ZoeDate zoeDate = new ZoeDate(year, month, day, hour, minute, second);
        return zoeDate;
    }

    public static boolean compare(Date time1, Date time2) {
        //Date类的一个方法，如果a早于b返回true，否则返回false
        if (time1.before(time2))
            return true;
        else
            return false;
        /*
        if(a.getTime()-b.getTime()<0)
            return true;
        else
            return false;
        */
    }

}
