package com.zoe.weiya.mq;

import com.zoe.weiya.AbstractTestCase;
import com.zoe.weiya.model.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by andy on 2017/1/10.
 */
public class MyMqGatwayTest extends AbstractTestCase{
    @Autowired
    MyMqGatway myMqGatway;

    @Test
    public void sendDataToCrQueue() throws Exception {
        User user = new User("111","123456");
        myMqGatway.sendDataToCrQueue(user);
    }

}