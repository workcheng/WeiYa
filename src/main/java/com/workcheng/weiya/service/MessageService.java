package com.workcheng.weiya.service;

import com.workcheng.weiya.common.constant.CommonConstant;
import com.workcheng.weiya.common.dto.BarrAgerModel;
import com.workcheng.weiya.common.dto.WsMessage;
import com.workcheng.weiya.common.exception.ServerInternalException;
import com.workcheng.weiya.common.utils.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andy
 * @date 2017/1/12
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {
    @Qualifier("redisTemplate0")
    @Autowired
    private RedisTemplate<String, WsMessage> zoeRedisTemplate0;
    private List<String> colors = new ArrayList() {
        {
            add("#CC3333");
            add("#996699");
            add("#FF6600");
            add("#0099CC");
            add("#3366CC");
            add("#000000");
            add("#FF6600");
            add("#339933");
            add("#9999FF");
            add("#660066");
            add("#FF33CC");
            add("#CCFF66");
        }
    };

    public Long save(WsMessage zoeMessage) {
        return zoeRedisTemplate0.opsForList().leftPush(CommonConstant.DANMU, zoeMessage);
    }

    public WsMessage get() {
        return zoeRedisTemplate0.opsForList().rightPop(CommonConstant.DANMU);
    }

    public BarrAgerModel getBarrAgerModel() throws ServerInternalException {
        BarrAgerModel barrAgerModel = new BarrAgerModel();
        WsMessage zoeMessage = zoeRedisTemplate0.opsForList().rightPop(CommonConstant.DANMU);
        if (null == zoeMessage) {
            throw new ServerInternalException("消息池为空");
        }
        barrAgerModel.setImg(zoeMessage.getHeadImgUrl());
        barrAgerModel.setMax(15);
        barrAgerModel.setSpeed(RandomUtil.getRandomInt(10));
        barrAgerModel.setInfo(zoeMessage.getContent());
        barrAgerModel.setColor(colors.get(RandomUtil.getRandomInt(11)));
        return barrAgerModel;
    }

    public Long count() {
        return zoeRedisTemplate0.opsForList().size(CommonConstant.DANMU);
    }

    public List<WsMessage> getAll() {
        return zoeRedisTemplate0.opsForList().range(CommonConstant.DANMU, 0, count());
    }
}
