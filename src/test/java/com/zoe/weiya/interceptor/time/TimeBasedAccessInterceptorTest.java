package com.zoe.weiya.interceptor.time;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by andy on 2016/12/21.
 */
public class TimeBasedAccessInterceptorTest {
    @Test
    public void preHandle() throws Exception {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        System.out.println(c.get(Calendar.DATE));
    }

}