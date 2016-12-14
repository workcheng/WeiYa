package com.zoe.weiya.controller;

import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.service.test.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by andy on 2016/12/14.
 */
@RequestMapping("mybatisMapper/test")
@RestController
public class TestController {
    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(TestController.class);
    @Autowired
    private TestService testService;

    @RequestMapping("")
    public Object test(){
        Map<String,Object> test = new HashMap<>();
        test.put("hi","man");
        return test;
    }

    @RequestMapping("mybatisMapper/test")
    public Object test2(){
        try {
            return testService.getTest();
        } catch (SQLException e) {
            log.error("error",e);
        }
        return null;
    }
}
