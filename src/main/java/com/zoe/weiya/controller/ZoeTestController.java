package com.zoe.weiya.controller;

import com.zoe.weiya.comm.exception.HasSignException;
import com.zoe.weiya.comm.exception.InternalException;
import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.model.OnlyUser;
import com.zoe.weiya.model.User;
import com.zoe.weiya.service.ZoeTestService;
import com.zoe.weiya.service.user.UserService;
import com.zoe.weiya.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by andy on 2016/12/14.
 */
@RequestMapping("zoeTest")
@RestController
public class ZoeTestController {
    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(ZoeTestController.class);
    @Autowired
    private ZoeTestService testService;
    @Autowired
    private UserService userService;

    @RequestMapping("hello")
    public Object test(@RequestParam(value = "name") String code){
        Map<String,Object> test = new HashMap<>();
        test.put("hi",code);
        return test;
    }

    @RequestMapping("test")
    public Object test2(@RequestParam(value = "test") String test){
        try {
            return testService.getTest();
        } catch (SQLException e) {
            log.error("error",e);
        }
        return null;
    }

    @RequestMapping(value = "redis",method = RequestMethod.POST)
    public void save(@RequestBody User u){
        try {
            userService.save(u);
        } catch (HasSignException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "redis",method = RequestMethod.GET)
    public OnlyUser get(String id){
        return userService.get(id);
    }

    @RequestMapping("")
    public void getIp(HttpServletRequest request, HttpServletResponse response){
        try {
            log.info("remote address  "+request.getRemoteAddr());
            log.info("remote host  "+request.getRemoteHost());
            log.info("remote port "+request.getRemotePort());
            log.info("remote user  "+request.getRemoteUser());

            log.info("local address  "+request.getLocalAddr());
            log.info("local port "+request.getLocalPort());
            log.info("local user  "+request.getLocalName());
            String requestRealIp = IpUtil.getRequestRealIp(request);
            log.info("requestRealIp="+requestRealIp);
            request.setAttribute("msg", requestRealIp);
            request.getRequestDispatcher("/test.jsp").forward(request, response);
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(requestRealIp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
