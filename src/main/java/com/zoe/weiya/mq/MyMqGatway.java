package com.zoe.weiya.mq;

import com.zoe.weiya.model.User;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by andy on 2017/1/10.
 */
public class MyMqGatway {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendDataToCrQueue(User obj) {
        amqpTemplate.convertAndSend("queue_one_key", obj);
    }
}