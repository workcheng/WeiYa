package com.zoe.weiya.util;

import com.zoe.weiya.comm.constant.CommonConstant;
import com.zoe.weiya.comm.properties.ZoeProperties;
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


    public static String getTime(ZoeDate now){
        if( now.getHour() < ZoeProperties.getNoonHour()){//14-小于中午的时间-早上（0:00-13:59:59）
            return CommonConstant.MORNING;
        }else if(now.getHour() >= ZoeProperties.getOffWorkHour()){//16-大于下班的时间-晚上(16:00:00-23:59:59)
            return CommonConstant.NIGHT;
        }else {//处于上面两个时间的中间，也就是-下午(14:00:00-15:59:59)
            return CommonConstant.NOON;
        }
    }

    public static String whichDay(ZoeDate now){
        ZoeDate startTime = ZoeProperties.getStartTime();
        String whichDay[] = {CommonConstant.FIRST_DAY, CommonConstant.SECOND_DAY, CommonConstant.THIRD_DAY};
        return whichDay[now.getDay()-startTime.getDay()];
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
