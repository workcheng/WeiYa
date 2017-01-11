package com.zoe.weiya.mq.stomp;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Program {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("192.168.1.1");
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);
        channel.exchangeDeclare("rabbitmq", "fanout",true);
//        channel.messageCount("1");
        String routingKey = "rabbitmq_routingkey";
        String message = "{\"id\":1200,\"name\":\"Welcome to RabbitMQ message push!\"}";
        channel.basicPublish("rabbitmq", routingKey, null, message.getBytes());
        System.out.println("[x] Sent Message:"+message);
//        channel.close();
//        connection.close();
    }

}