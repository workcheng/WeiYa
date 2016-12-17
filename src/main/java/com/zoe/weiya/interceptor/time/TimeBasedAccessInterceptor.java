package com.zoe.weiya.interceptor.time;

import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;

public class TimeBasedAccessInterceptor extends HandlerInterceptorAdapter {
  private static ZoeLogger log = ZoeLoggerFactory.getLogger(TimeBasedAccessInterceptor.class);
 
  private int    openingTime;
  private int    closingTime;
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

  @Override
  public boolean preHandle(HttpServletRequest request,
      HttpServletResponse response, Object handler) throws Exception {
    String url = request.getRequestURL().toString();
    log.error("url="+url);
    log.error("mappingURL="+mappingURL);
    if (mappingURL == null || url.matches(mappingURL)) {
      Calendar c = Calendar.getInstance();
      c.setTime(new Date());
      int now = c.get(Calendar.HOUR_OF_DAY);
      if (now < openingTime || now > closingTime) {
//        request.setAttribute("msg", "注册开放时间：9：00-12：00");
//        request.getRequestDispatcher("/msg.jsp").forward(request, response);
//        response.setContentType("application/json; charset=utf-8");
//        response.getWriter().write("注册开放时间：9：00-12：00");
//        return false;
        return true;
      }
      return true;
    }
    return true;
  }
}
