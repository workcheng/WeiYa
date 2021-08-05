package com.workcheng.weiya.handler;

import com.workcheng.weiya.builder.AbstractBuilder;
import com.workcheng.weiya.builder.ImageBuilder;
import com.workcheng.weiya.builder.TextBuilder;
import com.workcheng.weiya.common.dto.WxMenuKey;
import com.workcheng.weiya.service.WeiXinService;
import com.workcheng.weiya.common.utils.*;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

/**
 * @author Binary Wang
 */
@Component
public class MenuHandler extends AbstractHandler {
    @Autowired
    private MsgHandler msgHandler;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) {
        WeiXinService weixinService = (WeiXinService) wxMpService;
        String key = wxMessage.getEventKey();
        WxMenuKey menuKey;
        try {
            menuKey = JsonUtil.getJSON().readValue(key, WxMenuKey.class);
        } catch (Exception e) {
            return WxMpXmlOutMessage.TEXT().content(key)
                    .fromUser(wxMessage.getToUser())
                    .toUser(wxMessage.getFromUser()).build();
        }
        AbstractBuilder builder = null;
        switch (menuKey.getType()) {
            case XmlMsgType.TEXT:
                builder = new TextBuilder();
                break;
            case XmlMsgType.IMAGE:
                builder = new ImageBuilder();
                break;
            case XmlMsgType.VOICE:
                break;
            case XmlMsgType.VIDEO:
                break;
            case XmlMsgType.NEWS:
                wxMessage.setContent(menuKey.getContent());
                return msgHandler.handle(wxMessage, context, wxMpService, sessionManager);
            default:
                break;
        }
        if (builder != null) {
            try {
                return builder.build(menuKey.getContent(), wxMessage, weixinService);
            } catch (Exception e) {
                this.logger.error(e.getMessage(), e);
            }
        }
        return null;
    }
}
