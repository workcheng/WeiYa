package com.zoe.weiya.service.message;

import com.zoe.weiya.comm.exception.InternalException;
import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.comm.properties.ZoeProperties;
import com.zoe.weiya.model.LuckyUser;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
  Created by andy on 2016/12/22.
 **/
@Service
public class WechatService {
    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(WechatService.class);
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
                        .toUser(wxMessage.getFromUser())
                        .addArticle(article1)
                        .addArticle(article2).build();
                wxMpService.getKefuService().sendKefuMessage(message);
                return null;
            }
        };
        return test;
    }

    public WxMpMessageHandler sendVoteMessage() {
        WxMpMessageHandler test = new WxMpMessageHandler() {
            public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context,
                                            WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
                WxMpKefuMessage.WxArticle article1 = new WxMpKefuMessage.WxArticle();
                String url = MessageFormat.format(ZoeProperties.get("config/static/static.properties", "vote.url"),wxMessage.getFromUser());
                article1.setUrl(url);
                article1.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/bVoOkrvEGHqgetjIc7VcFoCWgLCNaTOnZaXvR9J04EgxMfbm3WM9OreMfTcMcKN8UFkWtDwUbiatU7Qtxsutglg/0?wx_fmt=png");
                article1.setDescription("为你喜爱的节目投票");
                article1.setTitle("投票");

                WxMpKefuMessage message = WxMpKefuMessage.NEWS()
                        .toUser(wxMessage.getFromUser()).addArticle(article1)
                        .build();
                wxMpService.getKefuService().sendKefuMessage(message);
                return null;
            }
        };
        return test;
    }

    public void sendMessage(String openId, String name, Integer degree) throws Exception{
        String[] degreeList = {"一","二","三"};
        String messageText = ZoeProperties.get("config/static/static.properties","prize.message");
        String format = MessageFormat.format(messageText, name, degreeList[degree]);
        WxMpKefuMessage message = WxMpKefuMessage.TEXT().content(format).toUser(openId).build();
        try {
            wxMpService.getKefuService().sendKefuMessage(message);
        } catch (WxErrorException e) {
            log.error("error",e);
            throw new InternalException(e);
        }
    }

    public void sendMessage(List<LuckyUser> luckyUsers) throws Exception{
        for (int i=0; i<luckyUsers.size(); i++){
            LuckyUser user = luckyUsers.get(i);
            this.sendMessage(user.getOpenId(),user.getName(),user.getDegree());
        }
    }

    public WxMpMessageHandler sendCommentMessage(){
        WxMpMessageHandler test = new WxMpMessageHandler() {
            public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context,
                                            WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
                String url = MessageFormat.format(ZoeProperties.get("config/static/static.properties", "comment.url"),wxMessage.getFromUser());
                WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
                item.setDescription("发送弹幕");
                item.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/bVoOkrvEGHqgetjIc7VcFoCWgLCNaTOnZaXvR9J04EgxMfbm3WM9OreMfTcMcKN8UFkWtDwUbiatU7Qtxsutglg/0?wx_fmt=png");
                item.setTitle("评论上墙");
                item.setUrl(url);

                WxMpXmlOutNewsMessage m = WxMpXmlOutMessage.NEWS()
                        .fromUser(wxMessage.getToUser())
                        .toUser(wxMessage.getFromUser())
                        .addArticle(item)
                        .build();
                return m;
            }
        };
        return test;
    }
}
