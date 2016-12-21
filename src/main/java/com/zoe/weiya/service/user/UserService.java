package com.zoe.weiya.service.user;

import com.zoe.weiya.comm.constant.ZoeErrorCode;
import com.zoe.weiya.comm.redis.ZoeRedisTemplete;
import com.zoe.weiya.comm.response.ResponseMsg;
import com.zoe.weiya.comm.response.ZoeObject;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by andy on 2016/12/20.
 */
@Service
public class UserService {
    private static final String USER = "user";
    @Autowired
    private ZoeRedisTemplete zoeRedisTemplete;

    public ResponseMsg save(WxMpUser u){
        Long aLong = this.saveInSet(u.getOpenId());
        if(aLong == 1){
            zoeRedisTemplete.setValue(u.getOpenId(),u);
            return ZoeObject.success();
        }else if(aLong == 0){
            return ZoeObject.failure(ZoeErrorCode.ERROR_HAS_SIGN);
        }
        return ZoeObject.failure(ZoeErrorCode.ERROR_INTERNAL);
    }

    public WxMpUser get(String openId){
        return (WxMpUser) zoeRedisTemplete.getValue(openId);
    }

    public Long saveInSet(String openId){
       return zoeRedisTemplete.setSet(USER,openId);
    }

    public Set<String> getUserSet(){
        return (Set)zoeRedisTemplete.getSet(USER);
    }

    public boolean isMember(String openId){
        return zoeRedisTemplete.isMember(USER, openId);
    }
    
    public Long getUserSize(){
        return zoeRedisTemplete.getSetSize(USER);
    }
}
