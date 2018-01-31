package com.zoe.weiya.interceptor.time;

import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.util.ZoeDateUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeBasedAccessInterceptor extends HandlerInterceptorAdapter {
    private static ZoeLogger log = ZoeLoggerFactory.getLogger(TimeBasedAccessInterceptor.class);

    private String oneAmOpeningTime;
    private String oneAmClosingTime;

    private String onePmOpeningTime;
    private String onePmClosingTime;

    private String twoAmOpeningTime;
    private String twoAmClosingTime;

    private String twoPmOpeningTime;
    private String twoPmClosingTime;

    private String finalOpeningTime;
    private String finalClosingTime;


    private String mappingURL; // 利用正则映射到需要拦截的路径

    public String getFinalClosingTime() {
        return finalClosingTime;
    }

    public void setFinalClosingTime(String finalClosingTime) {
        this.finalClosingTime = finalClosingTime;
    }

    public String getFinalOpeningTime() {
        return finalOpeningTime;
    }

    public void setFinalOpeningTime(String finalOpeningTime) {
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

    public String getOneAmClosingTime() {
        return oneAmClosingTime;
    }

    public void setOneAmClosingTime(String oneAmClosingTime) {
        this.oneAmClosingTime = oneAmClosingTime;
    }

    public String getOneAmOpeningTime() {
        return oneAmOpeningTime;
    }

    public void setOneAmOpeningTime(String oneAmOpeningTime) {
        this.oneAmOpeningTime = oneAmOpeningTime;
    }

    public String getOnePmClosingTime() {
        return onePmClosingTime;
    }

    public void setOnePmClosingTime(String onePmClosingTime) {
        this.onePmClosingTime = onePmClosingTime;
    }

    public String getOnePmOpeningTime() {
        return onePmOpeningTime;
    }

    public void setOnePmOpeningTime(String onePmOpeningTime) {
        this.onePmOpeningTime = onePmOpeningTime;
    }

    public String getTwoAmClosingTime() {
        return twoAmClosingTime;
    }

    public void setTwoAmClosingTime(String twoAmClosingTime) {
        this.twoAmClosingTime = twoAmClosingTime;
    }

    public String getTwoAmOpeningTime() {
        return twoAmOpeningTime;
    }

    public void setTwoAmOpeningTime(String twoAmOpeningTime) {
        this.twoAmOpeningTime = twoAmOpeningTime;
    }

    public String getTwoPmClosingTime() {
        return twoPmClosingTime;
    }

    public void setTwoPmClosingTime(String twoPmClosingTime) {
        this.twoPmClosingTime = twoPmClosingTime;
    }

    public String getTwoPmOpeningTime() {
        return twoPmOpeningTime;
    }

    public void setTwoPmOpeningTime(String twoPmOpeningTime) {
        this.twoPmOpeningTime = twoPmOpeningTime;
    }

    private String msgTime(Date now, Date openTime, Date closeTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd日 kk:mm:ss");
        //如果时间在这之内，则让其签到
        if (ZoeDateUtil.compare(openTime, now) && ZoeDateUtil.compare(now, closeTime)) {
            log.info("now=" + sdf.format(now));
            return "true";
        }
        String msg = "签到开放时间：{0}-{1}";
        String format = MessageFormat.format(msg, sdf.format(openTime), sdf.format(closeTime));
        return format;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        log.info("url=" + url);
        int successCount = 0;
        StringBuilder msgList = new StringBuilder();
        String msg = "";
        //如果匹配url，是所要控制的页面
        if (mappingURL == null || url.matches(mappingURL)) {
            Date date = new Date();
            String am1 = msgTime(date, sdf.parse(oneAmOpeningTime), sdf.parse(oneAmClosingTime));
            if (!am1.equals("true")) {
                msg = am1;
                msgList.append(msg);
                msgList.append("<br>");
            } else {
                successCount++;
            }
            String last = msgTime(date, sdf.parse(finalOpeningTime), sdf.parse(finalClosingTime));
            if (!last.equals("true")) {
                msg = last;
                msgList.append(msg);
                msgList.append("<br>");
            } else {
                successCount++;
            }
            if (successCount >= 1){
                return true;
            }
            request.setAttribute("msg", msgList);
            request.getRequestDispatcher("/msg.jsp").forward(request, response);
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(msg);
            return false;
        }
        return true;
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
