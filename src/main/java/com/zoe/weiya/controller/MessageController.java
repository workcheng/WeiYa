package com.zoe.weiya.controller;

import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.model.User;
import com.zoe.weiya.service.message.WechatService;
import com.zoe.weiya.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by andy on 2016/12/30.
 */
@RequestMapping("message")
@RestController
public class MessageController {
    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(MessageController.class);
    @Autowired
    WechatService wechatService;
    @Autowired
    UserService userService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void sendMessage(@RequestBody List<String> openIds){
        for (int i=0; i<openIds.size(); i++){
            try {
                User userInfo = userService.getUserInfo(openIds.get(i));
                if(null != userInfo){
                    wechatService.sendMessage(userInfo.getOpenId(),userInfo.getName(),0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
