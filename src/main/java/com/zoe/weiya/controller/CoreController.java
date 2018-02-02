//package com.zoe.weiya.controller;
//
//import com.zoe.weiya.comm.constant.CommonConstant;
//import com.zoe.weiya.comm.logger.ZoeLogger;
//import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
//import com.zoe.weiya.service.message.WechatService;
//import com.zoe.weiya.service.websocket.WebSocketService;
//import me.chanjar.weixin.common.api.WxConsts;
//import me.chanjar.weixin.common.exception.WxErrorException;
//import me.chanjar.weixin.common.session.WxSessionManager;
//import me.chanjar.weixin.mp.api.WxMpConfigStorage;
//import me.chanjar.weixin.mp.api.WxMpMessageHandler;
//import me.chanjar.weixin.mp.api.WxMpMessageRouter;
//import me.chanjar.weixin.mp.api.WxMpService;
//import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
//import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
//import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
//import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
//import me.chanjar.weixin.mp.bean.result.WxMpUser;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.annotation.PostConstruct;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Map;
//
///**
// * 接入微信后端模块
// * Created by andy on 2016/12/15.
// */
//@RequestMapping("core")
//@Controller
//public class CoreController {
//    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(CoreController.class);
//    @Autowired
//    protected WxMpServiceImpl wxMpService;·
//    @Autowired
//    protected WxMpMessageRouter wxMpMessageRouter;
//    @Autowired
//    protected WxMpConfigStorage wxMpConfigStorage;
//    @Autowired
//    protected WechatService wechatService;
//    @Autowired
//    private WebSocketService webSocketService;
//
//    @RequestMapping()
//    public void wechat(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            service(request, response, wxMpService);
//        } catch (ServletException e) {
//            log.error("error", e);
//            e.printStackTrace();
//        } catch (IOException e) {
//            log.error("error", e);
//            e.printStackTrace();
//        }
//    }
//
//    @PostConstruct
//    public void init() throws ServletException {
//        WxMpMessageHandler test = test();
//        wxMpMessageRouter
//                .rule().async(false).content("andy").handler(test).end()
//                .rule().async(false).content("智业尾牙签到").handler(wechatService.sendOutSignMessage()).end()//回复智业签到
//                .rule().async(false).content("签到").handler(wechatService.sendInSignMessage()).end()//回复签到
//                .rule().async(false).eventKey(CommonConstant.SIGN).handler(wechatService.sendInSignMessage()).end()//签到菜单事件
//                .rule().async(false).content("投票").handler(wechatService.sendVoteMessage()).end()//回复投票
//                .rule().async(false).eventKey(CommonConstant.VOTE).handler(wechatService.sendVoteMessage()).end()//回复投票
//                .rule().async(false).content("上墙").handler(wechatService.sendCommentMessage()).end()//回复投票
//                .rule().async(false).eventKey(CommonConstant.COMMENT).handler(wechatService.sendCommentMessage()).end()//投票菜单事件
//                .rule().async(false).content("节目单").handler(wechatService.sendCardMessage()).end()//回复投票
//                .rule().async(false).eventKey(CommonConstant.CARD).handler(wechatService.sendCardMessage()).end()//投票菜单事件
//                .rule().async(false).event(WxConsts.EventType.SUBSCRIBE).handler(wechatService.sendInSignMessage()).end()//关注事件
////                .rule().async(false).msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.EVT_SCAN).handler(wechatService.sendSignMessage()).end()//扫码事件
////                .rule().async(false).msgType(WxConsts.MASS_MSG_TEXT).handler(reply).end()
////                .rule().async(false).msgType(WxConsts.XML_MSG_EVENT).end()//过滤获取位置事件
//                .rule().async(false).handler(test).end();
//        ;
//    }
//
//    private void service(HttpServletRequest request, HttpServletResponse response, WxMpServiceImpl wxMpService)
//            throws ServletException, IOException {
//        response.setContentType("text/html;charset=utf-8");
//        response.setStatus(HttpServletResponse.SC_OK);
//        String signature = request.getParameter("signature");
//        String nonce = request.getParameter("nonce");
//        String timestamp = request.getParameter("timestamp");
//
//        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
//            // 消息签名不正确，说明不是公众平台发过来的消息
//            response.getWriter().println("非法请求警告");
//            return;
//        }
//        String echostr = request.getParameter("echostr");
//        if (StringUtils.isNotBlank(echostr)) {
//            // 说明是一个仅仅用来验证的请求，回显echostr
//            response.getWriter().println(echostr);
//            return;
//        }
//
//        String encryptType = StringUtils.isBlank(request.getParameter("encrypt_type")) ? "raw"
//                : request.getParameter("encrypt_type");
//
//        if ("raw".equals(encryptType)) {
//            // 明文传输的消息
//            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request.getInputStream());
//            WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
//            if (null != outMessage) {
//                response.getWriter().write(outMessage.toXml());
//            }
//            return;
//        }
//
//        if ("aes".equals(encryptType)) {
//            // 是aes加密的消息
//            String msgSignature = request.getParameter("msg_signature");
//            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(), wxMpConfigStorage,
//                    timestamp, nonce, msgSignature);
//            WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
//            if (null != outMessage) {
//                response.getWriter().write(outMessage.toEncryptedXml(wxMpConfigStorage));
//            }
//            return;
//        }
//
//        response.getWriter().println("不可识别的加密类型");
//        return;
//    }
//
//    private WxMpMessageHandler test() {
//        WxMpMessageHandler test = new WxMpMessageHandler() {
//            public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context,
//                                            WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
//                WxMpXmlOutTextMessage m = WxMpXmlOutMessage.TEXT().content("你的消息我们已收到，我们会尽快回复你的！").fromUser(wxMessage.getToUser())
//                        .toUser(wxMessage.getFromUser()).build();
//                return m;
//            }
//        };
//
//        return test;
//    }
//
//    private WxMpMessageHandler reply() {
//        WxMpMessageHandler test = new WxMpMessageHandler() {
//            public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context,
//                                            WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
//                try {
//                    WxMpUser wxMpUser = wxMpService.getUserService().userInfo(wxMessage.getFromUser());
//                    webSocketService.broadcast(wxMessage.getContent(),wxMpUser.getHeadImgUrl());//将微信消息组装的弹幕格式的消息传入websocket通道
//                } catch (Exception e) {
//                    log.error("error", e);
//                    e.printStackTrace();
//                }
//                return null;
//            }
//        };
//        return test;
//    }
//
//}
