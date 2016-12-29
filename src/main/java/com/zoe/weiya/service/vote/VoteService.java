package com.zoe.weiya.service.vote;

import com.zoe.weiya.comm.exception.HasVoteException;
import com.zoe.weiya.comm.exception.VoteException;
import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.comm.redis.ZoeRedisTemplate;
import me.chanjar.weixin.common.bean.result.WxError;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chenghui on 2016/12/29.
 */
@Service
public class VoteService {
    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(VoteService.class);
    private static final String VOTE_1 = "VOTE1";
    private static final String VOTE_2 = "VOTE2";

    @Autowired
    private ZoeRedisTemplate zoeRedisTemplate;
    @Autowired
    protected WxMpServiceImpl wxMpService;

    public void save(String openId) throws HasVoteException, VoteException, WxErrorException {
        try {
            WxMpUser wxMpUser = wxMpService.getUserService().userInfo(openId);
        } catch (WxErrorException e) {
            log.error("error",e);
            WxError error = e.getError();
            if(null != error){
               /* if(error.getErrorCode() > 0){
                    throw new VoteException();
                }else if(error.getErrorCode() == -1){
                    throw new WxErrorException(error);
                }*/
                throw new WxErrorException(error);
            }
        }
        Long add = zoeRedisTemplate.getSetOperations().add(VOTE_1, openId);
        if(add == 0){
            throw new HasVoteException();
        }else if(add != 1){
            throw new VoteException();
        }
    }
}
