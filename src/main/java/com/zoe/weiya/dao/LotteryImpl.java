package com.zoe.weiya.dao;

import com.zoe.weiya.model.User;
import com.zoe.weiya.util.RandomUtil;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * Created by zhanghao on 2016/12/22.
 */
@Repository("LotteryImpl")
public class LotteryImpl extends BaseDaoImpl {


    public User LotterySelect(String redisIP,String RedisKey) {

        //1.获取所有签到人员的信息
        Jedis jedis = new Jedis(redisIP);
        Map<String, String> lotteryPerson = jedis.hgetAll(RedisKey);
        //2.进行随机筛选出一条（抽奖）
        String appId = RandomUtil.getRandomKeyFromMap(lotteryPerson);
        String name = lotteryPerson.get(appId);
        //3.已经抽中的人员从名单中剔除
        jedis.hdel(RedisKey, appId);
        Map<String, String> newPerson = jedis.hgetAll(RedisKey);
        User user = new User();
        user.setOpenid(appId);
        user.setName(name);
        return user;
    }
}
