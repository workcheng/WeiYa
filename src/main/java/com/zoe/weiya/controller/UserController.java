package com.zoe.weiya.controller;

import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.comm.response.ZoeObject;
import com.zoe.weiya.service.user.UserService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by chenghui on 2016/12/20.
 */
@RequestMapping("user")
@RestController
public class UserController {
    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "save",method = RequestMethod.POST)
    public Object save(@RequestBody WxMpUser u){
        userService.save(u);
        return ZoeObject.success();
    }

    @RequestMapping(value = "getUser",method = RequestMethod.GET)
    public WxMpUser get(@RequestParam(value = "id") String openId){
        return userService.get(openId);
    }
}
