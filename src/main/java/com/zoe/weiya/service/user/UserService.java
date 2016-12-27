package com.zoe.weiya.service.user;

import com.zoe.weiya.comm.constant.ZoeErrorCode;
import com.zoe.weiya.comm.exception.HasSignException;
import com.zoe.weiya.comm.exception.InternalException;
import com.zoe.weiya.comm.redis.ZoeRedisTemplate;
import com.zoe.weiya.comm.response.ResponseMsg;
import com.zoe.weiya.comm.response.ZoeObject;
import com.zoe.weiya.model.OnlyUser;
import com.zoe.weiya.model.User;
import com.zoe.weiya.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by andy on 2016/12/20.
 */
@Service
public class UserService {
    private static final String USER = "user";
    @Autowired
    private ZoeRedisTemplate zoeRedisTemplate;

    public void save(User u) throws HasSignException, InternalException {
        Long aLong = this.saveInSet(u.getOpenId());
        if (aLong == 1) {
            OnlyUser onlyUser = new OnlyUser();
            onlyUser.setOpenId(u.getOpenId());
            onlyUser.setName(u.getName());
            onlyUser.setDepName(u.getDepName());
            onlyUser.setOrder(u.getOrder());
            onlyUser.setHeadImgUrl(u.getHeadImgUrl());
            onlyUser.setSignFlag(u.getSignFlag());
            zoeRedisTemplate.setValue(u.getOpenId(), onlyUser);
        } else if (aLong == 0) {
            throw new HasSignException(ZoeErrorCode.HAS_SIGN.getDescription());
        } else {
            throw new InternalException(ZoeErrorCode.ERROR_INTERNAL.getDescription());
        }
    }

    public void commitLotteryPerson(List<OnlyUser> users) {
        for (OnlyUser onlyUser : users) {
            onlyUser.setLucky(true);
            zoeRedisTemplate.setValue(onlyUser.getOpenId(), onlyUser);
        }
    }

    public void resetIsLuckyFlag(List<OnlyUser> users) {
        for (OnlyUser onlyUser : users) {
            onlyUser.setLucky(false);
            zoeRedisTemplate.setValue(onlyUser.getOpenId(), onlyUser);
        }
    }


    public ResponseMsg deleteAll(List<OnlyUser> users) {
        boolean flag = false;
        for (OnlyUser onlyUser : users) {
            zoeRedisTemplate.deleteHash(onlyUser.getOpenId());
            flag = true;
        }
        if (flag) {
            return ZoeObject.failure(ZoeErrorCode.SUCCESS);
        } else {
            return ZoeObject.failure(ZoeErrorCode.ERROR);
        }
    }

    public OnlyUser get(String openId) {
        return (OnlyUser) zoeRedisTemplate.getValue(openId);
    }

    public Long saveInSet(String openId) {
        return zoeRedisTemplate.setSet(USER, openId);
    }

    public Set<String> getOpenIdSet() {
        return (Set) zoeRedisTemplate.getSet(USER);
    }

    public boolean isMember(String openId) {
        return zoeRedisTemplate.isMember(USER, openId);
    }

    public Long getUserSize() {
        return zoeRedisTemplate.getSetSize(USER);
    }

    public List<OnlyUser> getSignUser() {
        Set<String> openIdSet = this.getOpenIdSet();
        List<OnlyUser> list = new ArrayList<>();
        if (null != openIdSet) {
            Iterator<String> i = openIdSet.iterator();//迭代
            while (i.hasNext()) {//遍历
                String openId = i.next();
                OnlyUser onlyUser = (OnlyUser) zoeRedisTemplate.getValue(openId);
                list.add(onlyUser);
            }
        }
        return list;
    }

    public Set<OnlyUser> getSignUserSet() {
        Set<String> idSet = this.getOpenIdSet();
        Set<OnlyUser> list = new HashSet<>();
        if (null != idSet) {
            Iterator<String> i = idSet.iterator();
            while (i.hasNext()) {
                String openId = i.next();
                OnlyUser onlyUser = (OnlyUser) zoeRedisTemplate.getValue(openId);
                list.add(onlyUser);
            }
        }
        return list;
    }

    //后端判断抽奖重复

    //签到的跟抽奖的分离出来，签到以五份数据保存，抽奖保存在一份，五个key->value


    public OnlyUser LotterySelect() {
        //1.获取所有签到人员的信息
        List<OnlyUser> signUser = getSignUser();
        List<OnlyUser> list = RandomUtil.createRandomList(signUser, 1);
        //分批次抽奖中奖名单
        Set<OnlyUser> priceUser = new HashSet<>();
        //2.进行随机筛选出一条（抽奖）
        User onlyUser = (User) list.get(0);

        priceUser.add(onlyUser);
        return onlyUser;

    }

}
