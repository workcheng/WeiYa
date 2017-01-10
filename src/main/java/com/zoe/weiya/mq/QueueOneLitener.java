package com.zoe.weiya.mq;

import com.zoe.weiya.model.User;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by andy on 2017/1/10.
 */
public class QueueOneLitener implements  MessageListener{
    private static final Logger log = Logger.getLogger(QueueOneLitener.class);
    @Override
    public void onMessage(Message message) {
        byte[] body = message.getBody();

        log.info(" data :" + body);

        try {
            User ob = null;
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(body));
            ob = (User) ois.readObject();
            log.info(" data :" + ob);
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}