package com.workcheng.weiya.controller;

import com.workcheng.weiya.common.constant.ErrorCode;
import com.workcheng.weiya.common.dto.Vote;
import com.workcheng.weiya.common.exception.HasVoteException;
import com.workcheng.weiya.common.exception.VoteException;
import com.workcheng.weiya.common.utils.ResponseUtil;
import com.workcheng.weiya.service.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 *
 * @author andy
 * @date 2016/12/29
 */
@RequestMapping("vote")
@RestController
@RequiredArgsConstructor
@Slf4j
public class VoteController {
    private final VoteService voteService;

    /**
     * 保存投票者信息
     *
     * @param vote
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Object vote(@RequestBody @Valid Vote vote) {
        try {
            voteService.save(vote.getId());
            return ResponseUtil.success();
        } catch (HasVoteException e) {
            log.error("error", e);
            return ResponseUtil.failure(ErrorCode.HAS_VOTE);
        } catch (VoteException e) {
            log.error("error", e);
            return ResponseUtil.failure(ErrorCode.ERROR_VOTE);
        } catch (WxErrorException e) {
            log.error("error", e);
            return ResponseUtil.failure(e.getError());
        }
    }
}
