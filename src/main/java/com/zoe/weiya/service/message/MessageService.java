package com.zoe.weiya.service.message;

import com.zoe.weiya.comm.constant.CommonConstant;
import com.zoe.weiya.comm.exception.InternalException;
import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.comm.redis.ZoeRedisTemplate;
import com.zoe.weiya.model.responseModel.BarrAgerModel;
import com.zoe.weiya.model.responseModel.ZoeMessage;
import com.zoe.weiya.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenghui on 2017/1/12.
 */
@Service
public class MessageService {
    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(MessageService.class);
    @Qualifier("zoeRedisTemplate0")
    @Autowired
    private ZoeRedisTemplate zoeRedisTemplate0;
    private List<String> colors = new ArrayList(){
        {
            add("#CC3333");
            add("#996699");
            add("#FFFF00");
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

    public Long save(ZoeMessage zoeMessage){
        return zoeRedisTemplate0.getListOperations().leftPush(CommonConstant.DANMU,zoeMessage);
    }

    public ZoeMessage get() {
        return (ZoeMessage)zoeRedisTemplate0.getListOperations().rightPop(CommonConstant.DANMU);
    }

    public BarrAgerModel getBarrAgerModel() throws InternalException {
        BarrAgerModel barrAgerModel = new BarrAgerModel();
        ZoeMessage zoeMessage = (ZoeMessage) zoeRedisTemplate0.getListOperations().rightPop(CommonConstant.DANMU);
        if(null == zoeMessage){
            throw new InternalException("消息池为空");
        }
        barrAgerModel.setImg(zoeMessage.getHeadImgUrl());
        barrAgerModel.setMax(15);
        barrAgerModel.setSpeed(RandomUtil.getRandomInt(10));
        barrAgerModel.setInfo(zoeMessage.getContent());
        barrAgerModel.setColor(colors.get(RandomUtil.getRandomInt(11)));
        return barrAgerModel;
    }

    public Long count(){
        return zoeRedisTemplate0.getListOperations().size(CommonConstant.DANMU);
    }

    public List<ZoeMessage> getAll(){
        return (List)zoeRedisTemplate0.getListOperations().range(CommonConstant.DANMU,0,count());
    }
}
