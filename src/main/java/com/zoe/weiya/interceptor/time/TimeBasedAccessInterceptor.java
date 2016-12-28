package com.zoe.weiya.interceptor.time;

import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TimeBasedAccessInterceptor extends HandlerInterceptorAdapter {
    private static ZoeLogger log = ZoeLoggerFactory.getLogger(TimeBasedAccessInterceptor.class);

    private int oneAmOpeningTime;
    private int oneAmClosingTime;
    private int oneClosingDay;
    private int onePmOpeningTime;
    private int onePmClosingTime;

    private int twoAmOpeningTime;
    private int twoAmClosingTime;
    private int twoClosingDay;
    private int twoPmOpeningTime;
    private int twoPmClosingTime;


    private int finalOpeningTime;
    private int finalClosingTime;


    private String mappingURL; // 利用正则映射到需要拦截的路径

    public int getFinalClosingTime() {
        return finalClosingTime;
    }

    public void setFinalClosingTime(int finalClosingTime) {
        this.finalClosingTime = finalClosingTime;
    }

    public int getFinalOpeningTime() {
        return finalOpeningTime;
    }

    public void setFinalOpeningTime(int finalOpeningTime) {
        this.finalOpeningTime = finalOpeningTime;
    }

    public static ZoeLogger getLog() {
        return log;
    }

    public static void setLog(ZoeLogger log) {
        TimeBasedAccessInterceptor.log = log;
    }


    public String getMappingURL() {
        return mappingURL;
    }

    public void setMappingURL(String mappingURL) {
        this.mappingURL = mappingURL;
    }

    public int getOneAmClosingTime() {
        return oneAmClosingTime;
    }

    public void setOneAmClosingTime(int oneAmClosingTime) {
        this.oneAmClosingTime = oneAmClosingTime;
    }

    public int getOneAmOpeningTime() {
        return oneAmOpeningTime;
    }

    public void setOneAmOpeningTime(int oneAmOpeningTime) {
        this.oneAmOpeningTime = oneAmOpeningTime;
    }

    public int getOneClosingDay() {
        return oneClosingDay;
    }

    public void setOneClosingDay(int oneClosingDay) {
        this.oneClosingDay = oneClosingDay;
    }

    public int getOnePmClosingTime() {
        return onePmClosingTime;
    }

    public void setOnePmClosingTime(int onePmClosingTime) {
        this.onePmClosingTime = onePmClosingTime;
    }

    public int getOnePmOpeningTime() {
        return onePmOpeningTime;
    }

    public void setOnePmOpeningTime(int onePmOpeningTime) {
        this.onePmOpeningTime = onePmOpeningTime;
    }

    public int getTwoAmClosingTime() {
        return twoAmClosingTime;
    }

    public void setTwoAmClosingTime(int twoAmClosingTime) {
        this.twoAmClosingTime = twoAmClosingTime;
    }

    public int getTwoAmOpeningTime() {
        return twoAmOpeningTime;
    }

    public void setTwoAmOpeningTime(int twoAmOpeningTime) {
        this.twoAmOpeningTime = twoAmOpeningTime;
    }

    public int getTwoClosingDay() {
        return twoClosingDay;
    }

    public void setTwoClosingDay(int twoClosingDay) {
        this.twoClosingDay = twoClosingDay;
    }

    public int getTwoPmClosingTime() {
        return twoPmClosingTime;
    }

    public void setTwoPmClosingTime(int twoPmClosingTime) {
        this.twoPmClosingTime = twoPmClosingTime;
    }

    public int getTwoPmOpeningTime() {
        return twoPmOpeningTime;
    }

    public void setTwoPmOpeningTime(int twoPmOpeningTime) {
        this.twoPmOpeningTime = twoPmOpeningTime;
    }

    private String msgTime(int now, int day, int matchingDay, int openTime, int closeTime) {
        if (now == matchingDay) {
            if (openTime < now && now < closeTime) {//如果时间在这之内，则让其签到
                log.info("closingDay=" + day);
                log.info("now=" + now);
                return "true";
            }
        }
        String msg = "签到开放时间：{0}�{1}:00-{2}:00";
        String format = MessageFormat.format(msg, matchingDay, openTime, closeTime);
        return format;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        int failureCount = 0;
        int successCount = 0;
        String msg = "";
        if (mappingURL == null || url.matches(mappingURL)) {//如果匹配url，是所要控制的页面
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            int now = c.get(Calendar.HOUR_OF_DAY);
            int day = c.get(Calendar.DATE);
//            if (day == oneClosingDay) {
//                if (oneAmOpeningTime < now && now < oneAmClosingTime) {//如果时间在这之内，则让其签到
//                    log.info("closingDay=" + oneClosingDay);
//                    log.info("now=" + now);
//                    return true;
//                } else if (onePmOpeningTime < now && now < onePmClosingTime) {
//                    log.info("closingDay=" + oneClosingDay);
//                    log.info("now=" + now);
//                    return true;
//                }

            String am1 = msgTime(now, day, oneClosingDay, oneAmOpeningTime, oneAmClosingTime);
            if (!am1.equals("true")) {
                failureCount++;
                msg = am1;
            } else {
                successCount++;
            }
            String pm1 = msgTime(now, day, oneClosingDay, onePmOpeningTime, onePmClosingTime);
            if (!pm1.equals("true")) {
                failureCount++;
                msg = pm1;
            } else {
                successCount++;
            }
            String am2 = msgTime(now, day, twoClosingDay, twoAmOpeningTime, twoAmClosingTime);
            if (!am2.equals("true")) {
                failureCount++;
                msg = am2;
            } else {
                successCount++;
            }
            String pm2 = msgTime(now, day, twoClosingDay, twoPmOpeningTime, twoPmClosingTime);
            if (!pm2.equals("true")) {
                failureCount++;
                msg = pm2;
            } else {
                successCount++;
            }
            String last = msgTime(now, day, twoClosingDay, finalOpeningTime, finalClosingTime);
            if (!last.equals("true")) {
                failureCount++;
                msg = last;
            } else {
                successCount++;
            }
            if (successCount >= 1 && failureCount < 5)
                return true;
        }

//            String msg = "签到开放时间：{0}�{1}:00-{2}:00";
//            String format = MessageFormat.format(msg, closingDay, openingTime, closingTime);
        request.setAttribute("msg", msg);
        request.getRequestDispatcher("/msg.jsp").forward(request, response);
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(msg);
        return false;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int day = c.get(Calendar.DATE);
        request.setAttribute("signDay", day);
        request.setAttribute("signTime", hour + ":" + minute);
    }
}
