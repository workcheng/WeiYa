package com.zoe.weiya.service.user;

import com.zoe.weiya.comm.constant.ZoeErrorCode;
import com.zoe.weiya.comm.redis.ZoeRedisTemplete;
import com.zoe.weiya.comm.response.ResponseMsg;
import com.zoe.weiya.comm.response.ZoeObject;
import com.zoe.weiya.model.OnlyUser;
import com.zoe.weiya.model.User;
import com.zoe.weiya.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by andy on 2016/12/20.
 */
@Service
public class UserService {
    private static final String USER = "user";
    @Autowired
    private ZoeRedisTemplete zoeRedisTemplete;

    public ResponseMsg save(User u) {
        Long aLong = this.saveInSet(u.getOpenId());
        if (aLong == 1) {
            OnlyUser onlyUser = new OnlyUser();
            onlyUser.setOpenId(u.getOpenId());
            onlyUser.setName(u.getName());
            onlyUser.setDepName(u.getDepName());
            onlyUser.setOrder(u.getOrder());
            onlyUser.setHeadImgUrl(u.getHeadImgUrl());
            zoeRedisTemplete.setValue(u.getOpenId(), onlyUser);
            return ZoeObject.success();
        } else if (aLong == 0) {
            return ZoeObject.failure(ZoeErrorCode.HAS_SIGN);
        }
        return ZoeObject.failure(ZoeErrorCode.ERROR_INTERNAL);
    }

    public ResponseMsg deleteAll(List<OnlyUser> users) {
        boolean flag = false;
        for (OnlyUser onlyUser : users) {
            zoeRedisTemplete.deleteHash(onlyUser.getOpenId());
            flag = true;
        }
        if (flag) {
            return ZoeObject.failure(ZoeErrorCode.SUCCESS);
        } else {
            return ZoeObject.failure(ZoeErrorCode.ERROR);
        }
    }

    public OnlyUser get(String openId) {
        return (OnlyUser) zoeRedisTemplete.getValue(openId);
    }

    public Long saveInSet(String openId) {
        return zoeRedisTemplete.setSet(USER, openId);
    }

    public Set<String> getOpenIdSet() {
        return (Set) zoeRedisTemplete.getSet(USER);
    }

    public boolean isMember(String openId) {
        return zoeRedisTemplete.isMember(USER, openId);
    }

    public Long getUserSize() {
        return zoeRedisTemplete.getSetSize(USER);
    }

    public List<OnlyUser> getSignUser() {
        Set<String> openIdSet = this.getOpenIdSet();
        List<OnlyUser> list = new ArrayList<>();
        if (null != openIdSet) {
            Iterator i = openIdSet.iterator();//先迭代出来
            while (i.hasNext()) {//遍历
                String openId = (String) i.next();
                OnlyUser onlyUser = (OnlyUser) zoeRedisTemplete.getValue(openId);
                list.add(onlyUser);
            }
        }
        return list;
    }

    public OnlyUser LotterySelect() {
        //1.获取所有签到人员的信息
        List<OnlyUser> signUser = getSignUser();
        List<OnlyUser> list = RandomUtil.createRandomList(signUser, 1);
        //2.进行随机筛选出一条（抽奖）
        OnlyUser onlyUser = list.get(0);
        //3.已经抽中的人员从名单中剔除
//        list.remove(onlyUser);
//        deleteAll(list);/**/
//        for (OnlyUser user : list) {
//            save((User) user);
//        }
        return onlyUser;
    }

}
