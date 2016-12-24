package com.zoe.weiya.service.message;

import com.zoe.weiya.comm.properties.ZoeProperties;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Map;

/**
  Created by andy on 2016/12/22.
 **/
@Service
public class WechatService {
    @Autowired
    protected WxMpServiceImpl wxMpService;

    public WxMpMessageHandler sendSignMessage() {
        WxMpMessageHandler test = new WxMpMessageHandler() {
            public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context,
                                            WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
                WxMpKefuMessage.WxArticle article1 = new WxMpKefuMessage.WxArticle();
                article1.setUrl("www.baidu.com");
                article1.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/bVoOkrvEGHqgetjIc7VcFoCWgLCNaTOnZaXvR9J04EgxMfbm3WM9OreMfTcMcKN8UFkWtDwUbiatU7Qtxsutglg/0?wx_fmt=png");
                article1.setDescription("晚会节目单");
                article1.setTitle("节目单");

                WxMpKefuMessage.WxArticle article2 = new WxMpKefuMessage.WxArticle();
                String url = MessageFormat.format(ZoeProperties.get("config/static/static.properties", "web.url"),wxMessage.getFromUser());
                article2.setUrl(url);
                article2.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/bVoOkrvEGHrvy0Mwuiaxmumnrgp3bqHdvmKkCXg4lJ1ajvD2yInIWbuBhJAM2IE5oc5UlSTxgV3onfXicjudos6g/0");
                article2.setDescription("签到表单");
                article2.setTitle("签到");

                WxMpKefuMessage message = WxMpKefuMessage.NEWS()
                        .toUser(wxMessage.getFromUser()).addArticle(article1)
                        .addArticle(article2).build();
                wxMpService.getKefuService().sendKefuMessage(message);
                return null;
            }
        };

        return test;
    }
}
