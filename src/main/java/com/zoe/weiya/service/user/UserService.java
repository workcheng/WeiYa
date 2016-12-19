package com.zoe.weiya.service.user;

import com.zoe.weiya.comm.redis.ZoeRedisTemplete;
import com.zoe.weiya.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by andy on 2016/12/20.
 */
@Service
public class UserService {
    @Autowired
    private ZoeRedisTemplete zoeRedisTemplete;

    public void save(User u){
        zoeRedisTemplete.setValue(u.getId(),u);
    }

    public User get(String id){
        return (User) zoeRedisTemplete.getValue(id);
    }
}
