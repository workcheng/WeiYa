package com.zoe.weiya.service.websocket;

import com.zoe.weiya.comm.exception.InternalException;
import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.controller.echo.MyMessageInbound;
import com.zoe.weiya.model.responseModel.ZoeMessage;
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

    public void broadcast(String message, String headImgUrl, String openId) throws Exception {
        ZoeMessage zoeMessage = new ZoeMessage();
        String replaceMessage = sensitiveService.replaceSensitiveWord(message, 1, "*");
        zoeMessage.setContent(replaceMessage);
        zoeMessage.setHeadImgUrl(headImgUrl);
        zoeMessage.setId(openId);
        broadcast(JacksonJsonUtil.beanToJson(zoeMessage));
    }

    //TODO fix bug：建立多次websocket連接，衹能第一次鏈接能接受到信息
    public void broadcast(String ZoeMessageJsonString) throws Exception {//将消息传入websocket通道中
        ServletContext application = null;
        HttpServletRequest request;
        try {
            request = ZoeUtil.getHttpServletRequest();
            application = request.getServletContext();
        } catch (Exception e) {
            log.error("error", e);
            e.printStackTrace();
        }
        if (null == application) {
            throw new InternalException("获取不到httpRequest");
        }

        Set<MyMessageInbound> connections =
                (Set<MyMessageInbound>) application.getAttribute("connections");
        if (connections == null) {
            return;
        }
        CharBuffer buffer = CharBuffer.wrap(ZoeMessageJsonString);
        for (MyMessageInbound connection : connections) {
            try {
                connection.getWsOutbound().writeTextMessage(buffer);
            } catch (IOException e) {
                log.error("error", e);
            }
        }
    }
}
