package com.zoe.weiya.mq;

import com.zoe.weiya.service.websocket.WebSocketService;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.Charset;

/**
 * Created by andy on 2017/1/10.
 */
public class QueueOneLitener implements  MessageListener{
    private static final Logger log = Logger.getLogger(QueueOneLitener.class);
    @Autowired
    WebSocketService webSocketService;

    @Override
    public void onMessage(Message message) {

        byte[] body = message.getBody();
        String s;
        String s2;
        log.info(" data :" + body);
        if(body == null) {
            return ;
        } else {
            try {
                String contentType = message.getMessageProperties() != null?message.getMessageProperties().getContentType():null;
                if("application/x-java-serialized-object".equals(contentType)) {
                    s = SerializationUtils.deserialize(body).toString();
                    log.info(s);
                    return;
                }

                if("text/plain".equals(contentType) || "application/json".equals(contentType) || "text/x-json".equals(contentType) || "application/xml".equals(contentType)) {
                    s = new String(body, Charset.defaultCharset().name());
                    webSocketService.broadcast(s);
                    return;
                }
            } catch (Exception e) {
                log.error("error",e);
                e.printStackTrace();
            }
            s2 = body.toString() + "(byte[" + body.length + "])";
            log.info(s2);
        }
    }
}