package com.zoe.weiya.controller;

import com.zoe.weiya.service.message.WechatService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by andy on 2016/12/15.
 */
@RequestMapping("core")
@Controller
public class CoreController {

    @Autowired
    protected WxMpServiceImpl wxMpService;
    @Autowired
    protected WxMpMessageRouter wxMpMessageRouter;
    @Autowired
    protected WxMpConfigStorage wxMpConfigStorage;
    @Autowired
    protected WechatService wechatService;

    @RequestMapping("")
    public void wechat(HttpServletRequest request, HttpServletResponse response) {
        try {
            init();
            service(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() throws ServletException {
        WxMpMessageHandler test = test();
        WxMpMessageHandler reply = reply();
        wxMpMessageRouter
            .rule().async(false).content("andy").handler(test).end()
            .rule().async(false).content("签到").handler(wechatService.sendSignMessage()).end()//回复签到
            .rule().async(false).content("投票").handler(wechatService.sendVoteMessage()).end()//回复投票
            .rule().async(false).event(WxConsts.EVT_SUBSCRIBE).handler(wechatService.sendSignMessage()).end()//关注事件
            .rule().async(false).msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.EVT_SCAN).handler(wechatService.sendSignMessage()).end()//扫码事件
            .rule().async(false).handler(reply).end()
        ;
    }

    private void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String signature = request.getParameter("signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");

        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            // 消息签名不正确，说明不是公众平台发过来的消息
            response.getWriter().println("非法请求警告");
            return;
        }

        String echostr = request.getParameter("echostr");
        if (StringUtils.isNotBlank(echostr)) {
            // 说明是一个仅仅用来验证的请求，回显echostr
            response.getWriter().println(echostr);
            return;
        }

        String encryptType = StringUtils.isBlank(request.getParameter("encrypt_type")) ? "raw"
                : request.getParameter("encrypt_type");

        if ("raw".equals(encryptType)) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request.getInputStream());
            WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
             if (null != outMessage) {
                response.getWriter().write(outMessage.toXml());
             }
            return;
        }

        if ("aes".equals(encryptType)) {
            // 是aes加密的消息
            String msgSignature = request.getParameter("msg_signature");
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(), wxMpConfigStorage,
                    timestamp, nonce, msgSignature);
            WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
            if (null != outMessage) {
                response.getWriter().write(outMessage.toEncryptedXml(wxMpConfigStorage));
            }
            return;
        }

        response.getWriter().println("不可识别的加密类型");
        return;
    }

    private WxMpMessageHandler test() {
        WxMpMessageHandler test = new WxMpMessageHandler() {
            public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context,
                                            WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
                WxMpXmlOutTextMessage m = WxMpXmlOutMessage.TEXT().content("andy").fromUser(wxMessage.getToUser())
                        .toUser(wxMessage.getFromUser()).build();
                return m;
            }
        };

        return test;
    }

    private WxMpMessageHandler reply() {
        WxMpMessageHandler test = new WxMpMessageHandler() {
            public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context,
                                            WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
                WxMpXmlOutTextMessage m = WxMpXmlOutMessage.TEXT().content("维护中。。。").fromUser(wxMessage.getToUser())
                        .toUser(wxMessage.getFromUser()).build();
                return m;
            }
        };
        return test;
    }
}
