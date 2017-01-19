package com.zoe.weiya.service.message;

import com.zoe.weiya.comm.exception.InternalException;
import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.comm.properties.ZoeProperties;
import com.zoe.weiya.model.LuckyUser;
import com.zoe.weiya.service.user.UserService;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import org.apache.commons.lang3.StringUtils;
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
    @Autowired
    protected UserService userService;

    public WxMpMessageHandler sendInSignMessage() {
        WxMpMessageHandler test = new WxMpMessageHandler() {
            public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context,
                                            WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
                String url = MessageFormat.format(ZoeProperties.get("config/static/static.properties", "sign.in.url"),wxMessage.getFromUser());
                WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
                item.setDescription("签到表单");
                item.setPicUrl("https://mmbiz.qlogo.cn/mmbiz_jpg/rFTQWsGze4G89XqNehSdSBGt1ic6ricfgBvaxnXGhsicia3xhIaKJB0hWMIqDXqLXC3OmBKnwfMiaXaBbrECialjOJiaQ/0?wx_fmt=jpeg");
                item.setTitle("签到");
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

    public WxMpMessageHandler sendOutSignMessage() {
        WxMpMessageHandler test = new WxMpMessageHandler() {
            public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context,
                                            WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
                String url = MessageFormat.format(ZoeProperties.get("config/static/static.properties", "sign.out.url"),wxMessage.getFromUser());
                WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
                item.setDescription("签到表单");
                item.setPicUrl("https://mmbiz.qlogo.cn/mmbiz_jpg/rFTQWsGze4G89XqNehSdSBGt1ic6ricfgBvaxnXGhsicia3xhIaKJB0hWMIqDXqLXC3OmBKnwfMiaXaBbrECialjOJiaQ/0?wx_fmt=jpeg");
                item.setTitle("签到");
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

    public WxMpMessageHandler sendVoteMessage() {
        WxMpMessageHandler test = new WxMpMessageHandler() {
            public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context,
                                            WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
                String url = MessageFormat.format(ZoeProperties.get("config/static/static.properties", "vote.url"),wxMessage.getFromUser());
                WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
                item.setDescription("来投票吧");
                item.setPicUrl("https://mmbiz.qlogo.cn/mmbiz_jpg/rFTQWsGze4EdewBW92AAD6Ap8ydAQrgBnndVMdAIXB4CmGiaGiassibiaKhWID6icmdMg3kvWSejFd5omyUdjcvb0GA/0?wx_fmt=jpeg");
                item.setTitle("投票");
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

    public void sendMessage(LuckyUser user) throws Exception{
        if(null != user){
            if(StringUtils.isBlank(user.getOpenId()) || null == user.getDegree() || StringUtils.isBlank(user.getName())){
                throw new InternalException("非法id="+user.getOpenId()+"，degree="+user.getDegree()+"，name="+user.getName());
            }
        }
        String[] degreeList = {"一","二","三"};
        String messageText = ZoeProperties.get("config/static/static.properties","prize.message");
        String format = MessageFormat.format(messageText, user.getName(), degreeList[user.getDegree()]);
        WxMpKefuMessage message = WxMpKefuMessage.TEXT().content(format).toUser(user.getOpenId()).build();
        try {
            userService.saveMessage(user,format);
            wxMpService.getKefuService().sendKefuMessage(message);
        } catch (WxErrorException e) {
            log.error("error",e);
            throw new InternalException(e);
        }
    }

    public void sendMessage(List<LuckyUser> luckyUsers) throws Exception{
        for (int i=0; i<luckyUsers.size(); i++){
            LuckyUser user = luckyUsers.get(i);
            this.sendMessage(user);
        }
    }

    public WxMpMessageHandler sendCommentMessage(){
        WxMpMessageHandler test = new WxMpMessageHandler() {
            public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context,
                                            WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
                String url = MessageFormat.format(ZoeProperties.get("config/static/static.properties", "comment.url"),wxMessage.getFromUser());
                WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
                item.setDescription("发送弹幕");
                item.setPicUrl("https://mmbiz.qlogo.cn/mmbiz_jpg/rFTQWsGze4G89XqNehSdSBGt1ic6ricfgBfr8ThJnpIIibwpPhGjGrKpraiaNULFLfv238cC3sIxgCYZza6TYLKicBg/0?wx_fmt=jpeg");
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

    public WxMpMessageHandler sendCardMessage(){
        WxMpMessageHandler test = new WxMpMessageHandler() {
            public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context,
                                            WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
                String url = MessageFormat.format(ZoeProperties.get("config/static/static.properties", "card.url"),wxMessage.getFromUser());
                WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
                item.setDescription("年会节目单");
                item.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/bVoOkrvEGHqgetjIc7VcFoCWgLCNaTOnZaXvR9J04EgxMfbm3WM9OreMfTcMcKN8UFkWtDwUbiatU7Qtxsutglg/0?wx_fmt=png");
                item.setTitle("节目单");
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
