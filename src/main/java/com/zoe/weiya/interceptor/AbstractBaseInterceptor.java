package com.zoe.weiya.interceptor;

import com.zoe.weiya.comm.constant.CommonConstant;
import com.zoe.weiya.comm.context.SysContext;
import com.zoe.weiya.comm.uuid.ZoeUUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractBaseInterceptor extends HandlerInterceptorAdapter{

  public void doFilter(ServletRequest req, ServletResponse res)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    String trakcingId = request.getHeader(CommonConstant.TRACKING_ID);
    if (StringUtils.isBlank(trakcingId)) {
      trakcingId = ZoeUUID.random();
      SysContext.setTrackingId(trakcingId);
      response.setHeader(CommonConstant.TRACKING_ID, trakcingId);
    }    
  }

  protected String getToken(HttpServletRequest request, HttpServletResponse response) {
    String token = request.getHeader("ZG-TOKEN");
    if (null == token) {
      Object sessionToken = request.getSession().getAttribute("token");
      if (null != sessionToken) {
        token = sessionToken.toString();
      }
    }
    return token;
  }
}
