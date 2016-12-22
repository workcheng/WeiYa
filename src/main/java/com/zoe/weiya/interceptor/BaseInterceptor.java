package com.zoe.weiya.interceptor;

import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseInterceptor extends AbstractBaseInterceptor{
    
  private static ZoeLogger log = ZoeLoggerFactory.getLogger(BaseInterceptor.class);

    /**
     * This implementation always returns {@code true}.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
      doFilter(request,response);
      return true;
    }

    /**
     * This implementation is empty.
     */
    @Override
    public void postHandle(
        HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
        throws Exception {
    }

    /**
     * This implementation is empty.
     */
    @Override
    public void afterCompletion(
        HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {
    }

    /**
     * This implementation is empty.
     */
    @Override
    public void afterConcurrentHandlingStarted(
        HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
    }
}
