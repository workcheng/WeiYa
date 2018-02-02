package com.zoe.weiya.controller;

import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.comm.response.ZoeObject;
import com.zoe.weiya.handler.MsgHandler;
import com.zoe.weiya.model.LuckyUser;
import com.zoe.weiya.model.responseModel.BarrAgerModel;
import com.zoe.weiya.model.responseModel.ZoeMessage;
import com.zoe.weiya.service.WeixinService;
import com.zoe.weiya.service.message.MessageService;
import com.zoe.weiya.service.sensitative.SensitivewordFilter;
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
import javax.validation.Valid;
import java.util.List;

/**
 *
 * @author andy
 * @date 2016/12/30
 */
@RequestMapping("message")
@RestController
public class MessageController {
    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(MessageController.class);
    @Autowired
    MsgHandler msgHandler;
    @Autowired
    UserService userService;
    @Autowired
    WebSocketService webSocketService;
    @Autowired
    HttpServletRequest request;
    @Autowired
    MessageService messageService;
    @Autowired
    WeixinService weixinService;
    @Autowired
    protected SensitivewordFilter sensitiveService;

    /**
     * 公众号发送消息给用户，前提是24小时之内有交互
     * @param users
     * @return
     */
    @RequestMapping(value = "sendMsg", method = RequestMethod.POST)
    public Object sendMessage(@RequestBody @Valid List<LuckyUser> users) {
        try {
            msgHandler.sendLuckyMessage(users, weixinService.getKefuService(),userService);
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

    /**
     * 头像圆角化
     * @param headUrl
     * @return
     */
    private String getRadiusHeadImgUrl(String headUrl) {
        String defaultHeadImage = "http://" + request.getServerName() //服务器地址
                + ":"
                + request.getServerPort()           //端口号
                + request.getContextPath()      //项目名称
                + "/user/headImgUrl?url="+headUrl; //图片地址
        return defaultHeadImage;
    }

    @RequestMapping(value = "danmu", method = RequestMethod.POST)
    public Object sendDanmuMessage(@RequestBody @Validated ZoeMessage zoeMessage) {
        int count = 100;
        if (null != zoeMessage.getContent() && zoeMessage.getContent().length() > count) {
            return ZoeObject.failure("字符长度不能大于" + count + "个");
        }
        if (ZoeCrossSiteScriptingValidation.IsDangerousString(zoeMessage.getContent())) {
            return ZoeObject.failure("非法字符");
        }
        if (StringUtils.isBlank(zoeMessage.getHeadImgUrl())) {
            zoeMessage.setHeadImgUrl(getDefaultHeadImgUrl());
        }
        zoeMessage.setHeadImgUrl(getRadiusHeadImgUrl(zoeMessage.getHeadImgUrl()));
        try {
            //for (int i = 0; i <= 100; i++) {
            webSocketService.broadcast(zoeMessage.getContent(), zoeMessage.getHeadImgUrl());
            //}
            return ZoeObject.success();
        } catch (Exception e) {
            log.error("error", e);
            return ZoeObject.failure(e.getMessage());
        }
    }

    /**
     * 把弹幕消息存储到数据库中
     * @param zoeMessage
     * @return
     */
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

    /**
     * 从数据库中获取最开始的一条弹幕消息
     * @return
     */
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
