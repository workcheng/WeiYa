package com.workcheng.weiya.common.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * ws操作相关服务
 * @author andy
 * @Date: 2021/5/25 17:52
 */
@Service
@Slf4j
public class WsService {

    /**
     * 发送消息
     *
     * @param session
     * @param text
     * @return
     * @throws IOException
     */
    public void sendMsg(WebSocketSession session, String text) throws IOException {
        session.sendMessage(new TextMessage(text));
    }

    /**
     * 广播消息
     *
     * @param text
     * @return
     * @throws IOException
     */
    public void broadcastMsg(String text) throws IOException {
        for (WebSocketSession session : WsSessionManager.SESSION_POOL.values()) {
            session.sendMessage(new TextMessage(text));
        }
    }
}
