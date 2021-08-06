package com.workcheng.weiya.controller;

import com.workcheng.weiya.common.domain.LuckyUser;
import com.workcheng.weiya.common.dto.BarrAgerModel;
import com.workcheng.weiya.common.dto.WsMessage;
import com.workcheng.weiya.common.utils.ResponseUtil;
import com.workcheng.weiya.common.utils.ZoeCrossSiteScriptingValidationUtil;
import com.workcheng.weiya.common.handler.MsgHandler;
import com.workcheng.weiya.service.MessageService;
import com.workcheng.weiya.service.UserService;
import com.workcheng.weiya.service.WeiXinService;
import com.workcheng.weiya.service.sensitative.SensitiveWordFilter;
import com.workcheng.weiya.common.websocket.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author andy
 * @date 2016/12/30
 */
@RequestMapping("message")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MessageController {
    private final MsgHandler msgHandler;
    private final UserService userService;
    private final WebSocketService webSocketService;
    private final HttpServletRequest request;
    private final MessageService messageService;
    private final WeiXinService weixinService;
    private final SensitiveWordFilter sensitiveService;

    /**
     * 公众号发送消息给用户，前提是24小时之内有交互
     *
     * @param users
     * @return
     */
    @RequestMapping(value = "/sendMsg", method = RequestMethod.POST)
    public Object sendMessage(@RequestBody @Valid List<LuckyUser> users) {
        try {
            msgHandler.sendLuckyMessage(users, weixinService.getKefuService(), userService);
            return ResponseUtil.success();
        } catch (Exception e) {
            log.error("error", e);
            return ResponseUtil.failure(e.toString());
        }
    }

    @RequestMapping(value = "/danmu", method = RequestMethod.POST)
    public Object sendDanmuMessage(@RequestBody @Validated WsMessage zoeMessage) {
        int count = 100;
        if (null != zoeMessage.getContent() && zoeMessage.getContent().length() > count) {
            return ResponseUtil.failure("字符长度不能大于" + count + "个");
        }
        if (ZoeCrossSiteScriptingValidationUtil.isDangerousString(zoeMessage.getContent())) {
            return ResponseUtil.failure("非法字符");
        }
        if (StringUtils.isBlank(zoeMessage.getHeadImgUrl())) {
            zoeMessage.setHeadImgUrl(getDefaultHeadImgUrl());
        }
        zoeMessage.setHeadImgUrl(getRadiusHeadImgUrl(zoeMessage.getHeadImgUrl()));
        try {
            webSocketService.broadcast(zoeMessage.getContent(), zoeMessage.getHeadImgUrl());
            return ResponseUtil.success();
        } catch (Exception e) {
            log.error("error", e);
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 把弹幕消息存储到数据库中
     *
     * @param zoeMessage
     * @return
     */
    @RequestMapping(value = "/danmuy", method = RequestMethod.POST)
    public Object sendDanmu(@RequestBody @Validated WsMessage zoeMessage) {
        int count = 300;
        if (null != zoeMessage.getContent() && zoeMessage.getContent().length() > count) {
            return ResponseUtil.failure("字符长度不能大于" + count);
        }
        if (ZoeCrossSiteScriptingValidationUtil.isDangerousString(zoeMessage.getContent())) {
            return ResponseUtil.failure("非法字符");
        }
        String replaceMessage = sensitiveService.replaceSensitiveWord(zoeMessage.getContent(), 1, "*");
        zoeMessage.setContent(replaceMessage);
        try {
            Long save = messageService.save(zoeMessage);
            if (save > 0) {
                return ResponseUtil.success(save);
            } else {
                log.error("not save");
                return ResponseUtil.failure();
            }
        } catch (Exception e) {
            log.error("error", e);
            return ResponseUtil.failure(e);
        }
    }

    /**
     * 从数据库中获取最开始的一条弹幕消息
     *
     * @return
     */
    @RequestMapping(value = "/danmuy", method = RequestMethod.GET)
    public Object getDanmu() {
        try {
            BarrAgerModel barrAgerModel = messageService.getBarrAgerModel();
            if (StringUtils.isBlank(barrAgerModel.getImg())) {
                barrAgerModel.setImg(getDefaultHeadImgUrl());
            }
            return ResponseUtil.success(barrAgerModel);
        } catch (Exception e) {
            log.error("error", e);
            return ResponseUtil.failure(e);
        }
    }

    private String getDefaultHeadImgUrl() {
        return defaultPrefix() + "/danmu/images/wechat_logo.jpg";
    }

    /**
     * 头像圆角化
     *
     * @param headUrl
     * @return
     */
    private String getRadiusHeadImgUrl(String headUrl) {
        return defaultPrefix() + "/user/headImgUrl?url=" + headUrl;
    }

    private String defaultPrefix() {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}
