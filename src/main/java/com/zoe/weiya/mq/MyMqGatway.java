package com.zoe.weiya.mq;

import com.zoe.weiya.model.responseModel.ZoeMessage;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by andy on 2017/1/10.
 */
@Service
public class MyMqGatway {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendDataToCrQueue(ZoeMessage message) {
        amqpTemplate.convertAndSend("queue_one_key", message);
    }
}