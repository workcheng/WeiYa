package com.zoe.weiya.controller;

import com.rabbitmq.client.AMQP;
import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.comm.response.ZoeObject;
import com.zoe.weiya.model.LuckyUser;
import com.zoe.weiya.model.responseModel.BarrAgerModel;
import com.zoe.weiya.model.responseModel.ZoeMessage;
import com.zoe.weiya.mq.stomp.Program;
import com.zoe.weiya.service.message.MessageService;
import com.zoe.weiya.service.message.WechatService;
import com.zoe.weiya.service.sensitative.SensitivewordFilter;
import com.zoe.weiya.service.user.UserService;
import com.zoe.weiya.service.websocket.WebSocketService;
import com.zoe.weiya.util.ZoeCrossSiteScriptingValidation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

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
    //    @Autowired MyMqGatway myMqGatway;
//    @Autowired private AmqpTemplate amqpTemplate;
    @Autowired
    MessageService messageService;
    @Autowired
    protected SensitivewordFilter sensitiveService;

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

    private String getDefaultHeadImgUrl() {
        String defaultHeadImage = "http://" + request.getServerName() //服务器地址
                + ":"
                + request.getServerPort()           //端口号
                + request.getContextPath()      //项目名称
                + "/danmu/images/wechat_logo.jpg"; //图片地址
        return defaultHeadImage;
    }

    @RequestMapping(value = "danmu", method = RequestMethod.POST)
    public Object sendDanmuMessage(@RequestBody @Validated ZoeMessage zoeMessage) {
        int count = 10;
        if (null != zoeMessage.getContent() && zoeMessage.getContent().length() > count) {
            return ZoeObject.failure("字符长度不能大于" + count + "个");
        }
        if (ZoeCrossSiteScriptingValidation.IsDangerousString(zoeMessage.getContent())) {
            return ZoeObject.failure("非法字符");
        }
        if (StringUtils.isBlank(zoeMessage.getHeadImgUrl())) {
            zoeMessage.setHeadImgUrl(getDefaultHeadImgUrl());
        }
        try {
            //for (int i = 0; i <= 100; i++) {
            webSocketService.broadcast(zoeMessage.getContent(), zoeMessage.getHeadImgUrl());
            //}
            return ZoeObject.success();
        } catch (Exception e) {
            log.error("error", e);
            return ZoeObject.failure(e);
        }
    }

    @RequestMapping("danmux")
    public void sendDanmuMessage() {
        String message = "{\"id\":1200,\"name\":\"Welcome to RabbitMQ message push!\"}";
        AMQP.BasicProperties properties = new AMQP.BasicProperties();
        MessageProperties messageProperties = new MessageProperties();
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-message-ttl", 60000);
//        channel.queueDeclare("myqueue", false, false, false, args);
//        messageProperties.setDelay(1000);
        messageProperties.setExpiration("60000");
        messageProperties.setMessageCount(1);
        messageProperties.setReceivedDelay(10000);
        messageProperties.setDelay(1000);
        int prefetchCount = 1;
//        channel.basicQos(prefetchCount);
        org.springframework.amqp.core.Message message1 = new Message(message.getBytes(), messageProperties);
//        amqpTemplate.send(message1);
    }

    @RequestMapping("danmu-x")
    public void sendDanmuMessagex() {
        Program program = new Program();
        try {
            program.main(new String[]{});
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "danmuy", method = RequestMethod.POST)
    public Object sendDanmu(@RequestBody @Validated ZoeMessage zoeMessage) {
        int count = 300;
        if (null != zoeMessage.getContent() && zoeMessage.getContent().length() > count) {
            return ZoeObject.failure("字符长度不能大于" + count);
        }
        if (ZoeCrossSiteScriptingValidation.IsDangerousString(zoeMessage.getContent())) {
            return ZoeObject.failure("非法字符");
        }
        String replaceMessage = sensitiveService.replaceSensitiveWord(zoeMessage.getContent(), 1, "*");
        zoeMessage.setContent(replaceMessage);
        try {
            Long save = messageService.save(zoeMessage);
            if (save > 0) {
                return ZoeObject.success(save);
            } else {
                log.error("not save");
                return ZoeObject.failure();
            }
        } catch (Exception e) {
            log.error("error", e);
            return ZoeObject.failure(e);
        }
    }

    @RequestMapping(value = "danmuy", method = RequestMethod.GET)
    public Object getDanmu() {
        try {
            BarrAgerModel barrAgerModel = messageService.getBarrAgerModel();
            if (StringUtils.isBlank(barrAgerModel.getImg())) {
                barrAgerModel.setImg(getDefaultHeadImgUrl());
            }
            return ZoeObject.success(barrAgerModel);
        } catch (Exception e) {
            log.error("error", e);
            return ZoeObject.failure(e);
        }
    }
}
