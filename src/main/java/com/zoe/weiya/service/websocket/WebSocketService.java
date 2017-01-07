package com.zoe.weiya.service.websocket;

import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.controller.echo.MyMessageInbound;
import com.zoe.weiya.model.responseModel.ZoeMessage;
import com.zoe.weiya.service.sensitative.SensitiveWordInit;
import com.zoe.weiya.service.sensitative.SensitivewordFilter;
import com.zoe.weiya.util.JacksonJsonUtil;
import com.zoe.weiya.util.ZoeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Set;

/**
 * Created by andy on 2017/1/6.
 */
@Service
public class WebSocketService {
    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(WebSocketService.class);
    @Autowired
    protected SensitivewordFilter sensitiveService;
    @Autowired
    protected SensitiveWordInit sensitiveWordInit;

    public void broadcast(String message) {//将消息传入websocket通道中
        HttpServletRequest request = ZoeUtil.getHttpServletRequest();
        if (null != request) {
            ServletContext application = null;
            try {
                application = request.getServletContext();
            } catch (Exception e) {
                request = ZoeUtil.getHttpServletRequest();
                application = request.getServletContext();
            }
            Set<MyMessageInbound> connections =
                    (Set<MyMessageInbound>) application.getAttribute("connections");
            if (connections == null) {
                return;
            }

            for (MyMessageInbound connection : connections) {
                try {
                    String replaceMessage = sensitiveService.replaceSensitiveWord(message, 1, "*");
                    CharBuffer buffer = CharBuffer.wrap(replaceMessage);
                    connection.getWsOutbound().writeTextMessage(buffer);
                } catch (IOException e) {
                    log.error("error", e);
                    e.printStackTrace();
                }
            }
        }
    }

    public void broadcast(String message, String headImgUrl) {//将消息传入websocket通道中
        HttpServletRequest request = ZoeUtil.getHttpServletRequest();
        if (null != request) {
            ServletContext application = null;
            try {
                application = request.getServletContext();
            } catch (Exception e) {
                request = ZoeUtil.getHttpServletRequest();
                application = request.getServletContext();
            }
            Set<MyMessageInbound> connections =
                    (Set<MyMessageInbound>) application.getAttribute("connections");
            if (connections == null) {
                return;
            }
            for (MyMessageInbound connection : connections) {
                try {
                    ZoeMessage zoeMessage = new ZoeMessage();
                    String replaceMessage = sensitiveService.replaceSensitiveWord(message, 1, "*");
                    zoeMessage.setContent(replaceMessage);
                    zoeMessage.setHeadImgUrl(headImgUrl);
                    CharBuffer buffer = CharBuffer.wrap(JacksonJsonUtil.beanToJson(zoeMessage));
                    connection.getWsOutbound().writeTextMessage(buffer);
                } catch (IOException e) {
                    log.error("error", e);
                    e.printStackTrace();
                } catch (Exception e) {
                    log.error("error", e);
                    e.printStackTrace();
                }
            }
        }
    }
}
