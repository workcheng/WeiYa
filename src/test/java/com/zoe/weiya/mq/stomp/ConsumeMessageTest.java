package com.zoe.weiya.mq.stomp;

import com.zoe.weiya.AbstractTestCase;
import org.junit.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by chenghui on 2017/1/11.
 */
public class ConsumeMessageTest extends AbstractTestCase{
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void testListen() throws Exception {
        String message = "{\"name\":1200,\"name\":\"Welcome to RabbitMQ message push!\"}";
//        amqpTemplate.convertAndSend();
        String routingKey = "rabbitmq_routingkey";
        MessageProperties messageProperties = new MessageProperties();
        org.springframework.amqp.core.Message message1 = new Message(message.getBytes(), messageProperties);
//        amqpTemplate.send(message1);
        amqpTemplate.send(message1);
    }
}