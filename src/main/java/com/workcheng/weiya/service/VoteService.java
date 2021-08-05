package com.workcheng.weiya.service;

import com.workcheng.weiya.common.exception.HasVoteException;
import com.workcheng.weiya.common.exception.VoteException;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by chenghui on 2016/12/29.
 */
@Service
public class VoteService {
    private static final Logger log = LoggerFactory.getLogger(VoteService.class);
    private static final String VOTE_1 = "VOTE1";
    private static final String VOTE_2 = "VOTE2";

    @Autowired
    @Qualifier("redisTemplate0")
    private RedisTemplate zoeRedisTemplate0;
    @Autowired
    private WxMpServiceImpl wxMpService;

    public void save(String openId) throws HasVoteException, VoteException, WxErrorException {
        try {
            WxMpUser wxMpUser = wxMpService.getUserService().userInfo(openId);
        } catch (WxErrorException e) {
            log.error("error", e);
            WxError error = e.getError();
            if (null != error) {
                throw new WxErrorException(error);
            }
        }
        Long add = zoeRedisTemplate0.opsForSet().add(VOTE_1, openId);
        if (add == 0) {
            throw new HasVoteException();
        } else if (add != 1) {
            throw new VoteException();
        }
    }
}
