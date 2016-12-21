package com.zoe.weiya.controller;

import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.comm.response.ZoeObject;
import com.zoe.weiya.service.user.UserService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
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
        if(StringUtils.isBlank(u.getOpenId())){
            return ZoeObject.failure();
        }
        return ZoeObject.success(userService.save(u));
    }

    @RequestMapping(value = "getUser",method = RequestMethod.GET)
    public Object get(@RequestParam(value = "id") String openId){
        return ZoeObject.success(userService.get(openId));
    }

}
