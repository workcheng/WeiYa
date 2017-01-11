package com.zoe.weiya.mq.stomp;

/**
 * Created by chenghui on 2017/1/11.
 */
public class ConsumeMessage {
    public void listen(String foo) {
        System.out.println("invoke");
        System.out.println(foo);
    }
}
