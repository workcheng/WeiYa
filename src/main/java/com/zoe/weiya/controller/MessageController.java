package com.zoe.weiya.controller;

import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.comm.response.ZoeObject;
import com.zoe.weiya.model.LuckyUser;
import com.zoe.weiya.model.responseModel.ZoeMessage;
import com.zoe.weiya.mq.MyMqGatway;
import com.zoe.weiya.service.message.WechatService;
import com.zoe.weiya.service.user.UserService;
import com.zoe.weiya.service.websocket.WebSocketService;
import com.zoe.weiya.util.ZoeCrossSiteScriptingValidation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    WebSocketService webSocketService;
    @Autowired(required = false)
    HttpServletRequest request;
    @Autowired
    MyMqGatway myMqGatway;

    @RequestMapping(value = "sendMsg", method = RequestMethod.POST)
    public Object sendMessage(@RequestBody List<LuckyUser> users) {
        try {
            wechatService.sendMessage(users);
            return ZoeObject.success();
        } catch (Exception e) {
            log.error("error", e);
            return ZoeObject.failure(e.toString());
        }
    }

    @RequestMapping(value = "danmu", method = RequestMethod.POST)
    public Object sendDanmuMessage(@RequestBody @Validated ZoeMessage zoeMessage) {//将消息传入websocket通道中
        if (ZoeCrossSiteScriptingValidation.IsDangerousString(zoeMessage.getContent())) {
            return ZoeObject.failure("非法字符");
        }
        if (StringUtils.isBlank(zoeMessage.getHeadImgUrl())) {
            String defaultHeadImage = "http://" + request.getServerName() //服务器地址
                    + ":"
                    + request.getServerPort()           //端口号
                    + request.getContextPath()      //项目名称
                    + "/danmu/images/wechat_logo.jpg"; //图片地址
            zoeMessage.setHeadImgUrl(defaultHeadImage);
        }

        try {
            //for (int i = 0; i <= 100; i++) {
//            webSocketService.broadcast(zoeMessage.getContent(), zoeMessage.getHeadImgUrl());
            myMqGatway.sendDataToCrQueue(zoeMessage);
            //}
            return ZoeObject.success();
        } catch (Exception e) {
            log.error("error", e);
            return ZoeObject.failure(e);
        }
    }
}
