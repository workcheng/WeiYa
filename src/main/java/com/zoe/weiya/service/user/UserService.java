package com.zoe.weiya.service.user;

import com.zoe.weiya.comm.redis.ZoeRedisTemplete;
import com.zoe.weiya.model.User;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by andy on 2016/12/20.
 */
@Service
public class UserService {
    private static final String key = "user";
    @Autowired
    private ZoeRedisTemplete zoeRedisTemplete;

    public void save(WxMpUser u){
        zoeRedisTemplete.setValue(u.getOpenId(),u);
    }

    public WxMpUser get(String openId){
        return (WxMpUser) zoeRedisTemplete.getValue(openId);
    }

    public Long saveInSet(User u){
       return zoeRedisTemplete.setSet(key,u);
    }

    public Set<User> getUserSet(){
        return (Set)zoeRedisTemplete.getSet(key);
    }
}
