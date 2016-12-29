package com.zoe.weiya.controller;

import com.zoe.weiya.comm.constant.ZoeErrorCode;
import com.zoe.weiya.comm.exception.*;
import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.comm.response.ZoeObject;
import com.zoe.weiya.service.vote.VoteService;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by chenghui on 2016/12/29.
 */
@RequestMapping("vote")
@RestController
public class VoteController {
    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(VoteController.class);
    @Autowired
    private VoteService voteService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object vote(@RequestParam String id){
        try {
            voteService.save(id);
            return ZoeObject.success();
        } catch (HasVoteException e) {
            log.error("error",e);
            return ZoeObject.failure(ZoeErrorCode.HAS_VOTE);
        } catch (VoteException e) {
            log.error("error",e);
            return ZoeObject.failure(ZoeErrorCode.ERROR_VOTE);
        } catch (WxErrorException e) {
            log.error("error",e);
            return ZoeObject.failure(e.getError());
        }
    }
}
