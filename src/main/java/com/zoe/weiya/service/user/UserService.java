package com.zoe.weiya.service.user;

import com.zoe.weiya.comm.redis.ZoeRedisTemplete;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by andy on 2016/12/20.
 */
@Service
public class UserService {
    @Autowired
    private ZoeRedisTemplete zoeRedisTemplete;

    public void save(WxMpUser u){
        zoeRedisTemplete.setValue(u.getOpenId(),u);
    }

    public WxMpUser get(String openId){
        return (WxMpUser) zoeRedisTemplete.getValue(openId);
    }
}
