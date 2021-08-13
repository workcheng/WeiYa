package com.workcheng.weiya.common.websocket;

import com.workcheng.weiya.common.domain.WsMessage;
import com.workcheng.weiya.common.utils.*;
import com.workcheng.weiya.service.sensitative.SensitiveWordFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * @author andy
 * @date 2021/8/5 9:31
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketService {
    private final WsService wsService;
    private final SensitiveWordFilter sensitiveService;

    public void broadcast(String message, String headImgUrl) throws Exception {
        WsMessage zoeMessage = new WsMessage();
        String replaceMessage = sensitiveService.replaceSensitiveWord(message, 1, "*");
        zoeMessage.setContent(replaceMessage);
        zoeMessage.setHeadImgUrl(headImgUrl);
        broadcast(JsonUtil.getJSON().writeValueAsString(zoeMessage));
    }

    public void broadcast(String jsonString) throws Exception {
        //将消息传入websocket通道中
        wsService.broadcastMsg(jsonString);
    }
}
