package com.workcheng.weiya.service;


import com.workcheng.weiya.common.config.WeiYaConfig;
import com.workcheng.weiya.common.constant.CommonConstant;
import com.workcheng.weiya.common.domain.*;
import com.workcheng.weiya.common.dto.UserListCount;
import com.workcheng.weiya.common.exception.*;
import com.workcheng.weiya.repository.LuckyUserSetRepository;
import com.workcheng.weiya.repository.MessageRepository;
import com.workcheng.weiya.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by andy on 2016/12/20.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final WxMpServiceImpl wxMpService;
    private final WeiYaConfig weiYaConfig;
    private final MessageRepository messageRepository;
    private final LuckyUserSetRepository luckyUserSetRepository;
    private final UserRepository userRepository;

    public void save2(User u) throws ServerInternalException, HasSignException, WxErrorException, VoteException {
        if (StringUtils.isEmpty(u.getOpenId())) {
            throw new ServerInternalException("openid不能为空");
        }
        final User user = userRepository.findByOpenId(u.getOpenId());
        if (user != null) {
            log.warn("user:{}", user);
            throw new HasSignException("已经签到");
        }
        WxMpUser wxMpUser = wxMpService.getUserService().userInfo(u.getOpenId());
        u.setNickName(wxMpUser.getNickname());
        u.setHeadImgUrl(wxMpUser.getHeadImgUrl());
        userRepository.save(u);
    }

    public User get2(String openId) {
        return userRepository.findByOpenId(openId);
    }

    public User isMember2(String openId){
        return userRepository.findByOpenId(openId);
    }

    public Long getUserSize2() {
        return userRepository.count();
    }

    public List<User> randomUser2(Integer count) throws ServerInternalException, LotteryException {
        //TODO 数量超过返回错误
        List<User> userList = new ArrayList<>();
        Long luckySetSize = luckyUserSetRepository.count();
        Long userSize = userRepository.count();
        long l = userSize - luckySetSize;
        if (count > l) {
            throw new LotteryException(String.valueOf(l));
        }
        if (luckySetSize.equals(userSize)) {
            //TODO REVIEW
            throw new ServerInternalException("中奖池已满");
        }
        final List<User> users = userRepository.takeUserByRandom(count);
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            long inLuckySet = saveInLuckySet2(user.getOpenId());
            //存入中奖池
            if (inLuckySet == 0) {
                //已经获奖,重新抽
                final List<User> list = userRepository.takeUserByRandom(1);
                users.remove(user);
                users.add(list.get(0));
                i = i - 1;
                //移除-1，当前位置被占用
            } else if (inLuckySet == 1) {
                userList.add(user);
            }
        }
        return userList;
    }

    private Long saveInLuckySet2(String openId) {
        final LuckyUserSet user = luckyUserSetRepository.findByOpenId(openId);
        if (user != null) {
            // fail
            return 0L;
        }
        final LuckyUserSet set = new LuckyUserSet();
        set.setOpenId(openId);
        final LuckyUserSet save = luckyUserSetRepository.save(set);
        return 1L;
    }

    public Set<String> getLuckyOpenIdSet2() {
        final HashSet<String> set = new HashSet<>();
        final Iterable<LuckyUserSet> all = luckyUserSetRepository.findAll();
        for (LuckyUserSet l:
             all) {
            set.add(l.getOpenId());
        }
        return set;
    }

    public Long getLuckySetSize2() {
        return luckyUserSetRepository.count();
    }

    public UserListCount orderMealUserCountAndUserList2() {
        UserListCount userListCount = new UserListCount();
        Iterable<User> userList = userRepository.findAll();
        userListCount.setNow(new Date());
        userListCount.setOrderCount(userRepository.countAllByIsOrder(1));
        userListCount.setUsers(userList);
        return userListCount;
    }

    public List<User> orderMealUserList2() {
        return userRepository.getAllByIsOrder(1);
    }

    public Integer saveMessage2(LuckyUser luckyUser, String message) throws ServerInternalException {
        Set<String> luckyOpenIdSet = this.getLuckyOpenIdSet2();
        if (luckyOpenIdSet.contains(luckyUser.getOpenId())) {
            User user = userRepository.findByOpenId(luckyUser.getOpenId());
            LuckyUserMessage luckyUserMessage = new LuckyUserMessage();
            luckyUserMessage.setOpenId(luckyUser.getOpenId());
            luckyUserMessage.setName(luckyUser.getName());
            luckyUserMessage.setDegree(luckyUser.getDegree());
            luckyUserMessage.setDepName(user.getDepName());
            luckyUserMessage.setSignDate(user.getSignDate());
            luckyUserMessage.setMessage(message);
            luckyUserMessage.setNickName(user.getNickName());
            try {
                messageRepository.save(luckyUserMessage);
            } catch (Exception e) {
                log.error("save message error", e);
                return 0;
            }
            return 1;
        } else {
            throw new ServerInternalException("非法消息，该用户不存在中奖池中，openId=" + luckyUser.getOpenId() + ",name=" + luckyUser.getName());
        }
    }

    public Iterable<LuckyUserMessage> getAllMessage2() {
        return messageRepository.findAll();
    }

    public List<LuckyUserMessage> getMessageByDegree2(Integer degree) {
        return messageRepository.getAllByDegree(degree);
    }
}
