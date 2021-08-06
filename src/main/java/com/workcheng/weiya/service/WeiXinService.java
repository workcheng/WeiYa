package com.workcheng.weiya.service;

import com.workcheng.weiya.common.handler.*;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.kefu.result.WxMpKfOnlineList;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.constant.WxMpEventConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static me.chanjar.weixin.common.api.WxConsts.*;

/**
 * @author andy
 */
@Service
@RequiredArgsConstructor
public class WeiXinService extends WxMpServiceImpl {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final LogHandler logHandler;
    protected final NullHandler nullHandler;
    protected final KfSessionHandler kfSessionHandler;
    protected final StoreCheckNotifyHandler storeCheckNotifyHandler;
    private final LocationHandler locationHandler;
    private final MenuHandler menuHandler;
    private final MsgHandler msgHandler;
    private final UnsubscribeHandler unsubscribeHandler;
    private final SubscribeHandler subscribeHandler;
    private WxMpMessageRouter router;
    private final WxMpConfigStorage wxMpConfigStorage;

    @PostConstruct
    public void init() {
        super.setWxMpConfigStorage(wxMpConfigStorage);
        this.refreshRouter();
    }

    private void refreshRouter() {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(this);
        // 记录所有事件的日志
        newRouter.rule().handler(this.logHandler).next();
        // 接收客服会话管理事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(WxMpEventConstants.CustomerService.KF_CREATE_SESSION)
                .handler(this.kfSessionHandler).end();
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(WxMpEventConstants.CustomerService.KF_CLOSE_SESSION)
                .handler(this.kfSessionHandler).end();
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(WxMpEventConstants.CustomerService.KF_SWITCH_SESSION)
                .handler(this.kfSessionHandler).end();
        // 门店审核事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(WxMpEventConstants.POI_CHECK_NOTIFY)
                .handler(this.storeCheckNotifyHandler)
                .end();
        // 自定义菜单事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(MenuButtonType.CLICK).handler(this.getMenuHandler()).end();
        // 点击菜单连接事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(MenuButtonType.VIEW).handler(this.nullHandler).end();
        // 关注事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(EventType.SUBSCRIBE).handler(this.getSubscribeHandler())
                .end();
        // 取消关注事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(EventType.UNSUBSCRIBE).handler(this.getUnsubscribeHandler())
                .end();
        // 上报地理位置事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(EventType.LOCATION).handler(this.getLocationHandler()).end();
        // 接收地理位置消息
        newRouter.rule().async(false).msgType(XmlMsgType.LOCATION)
                .handler(this.getLocationHandler()).end();
        // 扫码事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(EventType.SCAN).handler(this.getScanHandler()).end();
        // 默认
        newRouter.rule().async(false).handler(this.getMsgHandler()).end();
        this.router = newRouter;
    }

    public WxMpXmlOutMessage route(WxMpXmlMessage message) {
        try {
            return this.router.route(message);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
        return null;
    }

    public boolean hasKefuOnline() {
        try {
            WxMpKfOnlineList kfOnlineList = this.getKefuService().kfOnlineList();
            return kfOnlineList != null && kfOnlineList.getKfOnlineList().size() > 0;
        } catch (Exception e) {
            this.logger.error("获取客服在线状态异常: " + e.getMessage(), e);
        }
        return false;
    }

    protected MenuHandler getMenuHandler() {
        return this.menuHandler;
    }

    protected SubscribeHandler getSubscribeHandler() {
        return this.subscribeHandler;
    }

    protected UnsubscribeHandler getUnsubscribeHandler() {
        return this.unsubscribeHandler;
    }

    protected AbstractHandler getLocationHandler() {
        return this.locationHandler;
    }

    protected MsgHandler getMsgHandler() {
        return this.msgHandler;
    }

    protected AbstractHandler getScanHandler() {
        return null;
    }

}
