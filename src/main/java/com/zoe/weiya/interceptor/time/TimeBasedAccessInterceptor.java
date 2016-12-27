package com.zoe.weiya.interceptor.time;

import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeBasedAccessInterceptor extends HandlerInterceptorAdapter {
    private static ZoeLogger log = ZoeLoggerFactory.getLogger(TimeBasedAccessInterceptor.class);

    private int openingTime;
    private int closingTime;
    private int closingDay;
    private String mappingURL; // 利用正则映射到需要拦截的路径

    public void setOpeningTime(int openingTime) {
        this.openingTime = openingTime;
    }

    public void setClosingTime(int closingTime) {
        this.closingTime = closingTime;
    }

    public void setMappingURL(String mappingURL) {
        this.mappingURL = mappingURL;
    }

    public void setClosingDay(int closingDay) {
        this.closingDay = closingDay;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        log.error("url=" + url);
        log.error("mappingURL=" + mappingURL);
        if (mappingURL == null || url.matches(mappingURL)) {//如果匹配url，是所要控制的页面
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            int now = c.get(Calendar.HOUR_OF_DAY);
            int day = c.get(Calendar.DATE);
            if (day == closingDay) {
                if (openingTime < now && now < closingTime) {//如果时间在这之内，则让其签到
                    log.info("closingDay=" + closingDay);
                    log.info("now=" + now);
                    return true;
                }
            }
            String msg = "签到开放时间：{0}号 {1}:00-{2}:00";
            String format = MessageFormat.format(msg, closingDay, openingTime, closingTime);
            request.setAttribute("msg", format);
            request.getRequestDispatcher("/msg.jsp").forward(request, response);
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(format);
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
        request.setAttribute("signDay",day);
        request.setAttribute("signTime", hour + ":" + minute);
    }
}
