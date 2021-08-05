package com.workcheng.weiya.service;


import com.workcheng.weiya.common.constant.CommonConstant;
import com.workcheng.weiya.common.constant.ErrorCode;
import com.workcheng.weiya.common.domain.*;
import com.workcheng.weiya.common.dto.UserListCount;
import com.workcheng.weiya.common.exception.*;
import com.workcheng.weiya.common.utils.*;
import com.workcheng.weiya.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by andy on 2016/12/20.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Qualifier("redisTemplate0")
    private final RedisTemplate RedisTemplate0;
    @Qualifier("redisTemplate1")
    private final RedisTemplate RedisTemplate1;
    @Qualifier("redisTemplate2")
    private final RedisTemplate RedisTemplate2;
    @Qualifier("redisTemplate3")
    private final RedisTemplate RedisTemplate3;
    @Qualifier("redisTemplate4")
    private final RedisTemplate RedisTemplate4;
    private final WxMpServiceImpl wxMpService;
    private List<RedisTemplate> RedisTemplateIndexList;
    private final DateUtil dateUtil;
    private final UserRepository userRepository;

    @PostConstruct
    private void init() {
        RedisTemplateIndexList = new ArrayList<RedisTemplate>() {
            private static final long serialVersionUID = 1L;
            {
                add(RedisTemplate0);
                add(RedisTemplate1);
                add(RedisTemplate2);
                add(RedisTemplate3);
                add(RedisTemplate4);
            }
        };
    }

    private RedisTemplate getRedisTemplate() throws NotStartException, ServerInternalException {
        try {
            return RedisTemplateIndexList.get(Integer.parseInt(dateUtil.getIndex()));
        } catch (IndexOutOfBoundsException e) {
            throw new ServerInternalException("时间设置不对");
        }
    }

    public void save(User u) throws NotStartException, ServerInternalException, HasSignException, WxErrorException, VoteException {
        if (StringUtils.isEmpty(u.getOpenId())) {
            throw new ServerInternalException("openid不能为空");
        }
        WxMpUser wxMpUser = wxMpService.getUserService().userInfo(u.getOpenId());
        u.setNickName(wxMpUser.getNickname());
        u.setHeadImgUrl(wxMpUser.getHeadImgUrl());
        Long add = this.getRedisTemplate().opsForSet().add(CommonConstant.USER, u.getOpenId());
        if (add == 0) {
            throw new HasSignException("已经签到");
        }
        this.getRedisTemplate().opsForValue().set(u.getOpenId(), u);
    }

    @Deprecated
    public void save0(User u) throws HasSignException, ServerInternalException, WxErrorException, NotStartException {
        WxMpUser wxMpUser = wxMpService.getUserService().userInfo(u.getOpenId());
        if (null != wxMpUser) {
            Long aLong = this.saveInSet(u.getOpenId());
            if (aLong == 1) {
                u.setNickName(wxMpUser.getNickname());
                getRedisTemplate().opsForValue().set(u.getOpenId(), u);
            } else if (aLong == 0) {
                throw new HasSignException(ErrorCode.HAS_SIGN.getDescription());
            } else {
                throw new ServerInternalException(ErrorCode.ERROR_INTERNAL.getDescription());
            }

        }
    }

    public void commitLotteryPerson(List<UnionUser> users) throws NotStartException, ServerInternalException {
        SimpleDateFormat sdf = new SimpleDateFormat();
        String format = sdf.format(new Date());
        getRedisTemplate().opsForValue().set(format, users);
    }

    public ResponseMsg deleteAll(List<UnionUser> users) throws NotStartException, ServerInternalException {
        boolean flag = false;
        for (UnionUser onlyUser : users) {
            getRedisTemplate().opsForHash().delete(onlyUser.getOpenId());
            flag = true;
        }
        if (flag) {
            return ResponseUtil.failure(ErrorCode.SUCCESS);
        } else {
            return ResponseUtil.failure(ErrorCode.ERROR);
        }
    }

    public User get(String openId) throws NotStartException, ServerInternalException {
        return (User) getRedisTemplate().opsForValue().get(openId);
    }

    public User get(int index, String openId) throws NotStartException, ServerInternalException {
        return (User) RedisTemplateIndexList.get(index).opsForValue().get(openId);
    }

    public Long saveInSet(String openId) throws NotStartException, ServerInternalException {
        return getRedisTemplate().opsForSet().add(CommonConstant.USER, openId);
    }

    public Set<String> getOpenIdSet() throws NotStartException, ServerInternalException {
        return (Set) getRedisTemplate().opsForSet().members(CommonConstant.USER);
    }

    public Set<String> getOpenIdSet(int index) throws NotStartException, ServerInternalException {
        return (Set) RedisTemplateIndexList.get(index).opsForSet().members(CommonConstant.USER);
    }

    public boolean isMember(String openId) throws NotStartException, ServerInternalException {
        return getRedisTemplate().opsForSet().isMember(CommonConstant.USER, openId);
    }

    public Long getUserSize() throws NotStartException, ServerInternalException {
        return getRedisTemplate().opsForSet().size(CommonConstant.USER);
    }

    public Set<String> randomOpenIdSet(Long count) throws ServerInternalException, NotStartException {
        Set<String> set = new HashSet<String>();
        set.addAll(randomOpenIds(count));
        return set;
    }

    public List<String> randomOpenIds(Long count) throws ServerInternalException, NotStartException {
        return (List) getRedisTemplate().opsForSet().randomMembers(CommonConstant.USER, count);
    }

    public List<User> randomUsers(Integer count) throws ServerInternalException, NotStartException {
        List<User> result = new ArrayList<>();
        List<String> list = randomOpenIds(Long.valueOf(count));
        for (String s : list) {
            User user = (User) getRedisTemplate().opsForValue().get(s);
            result.add(user);
        }
        return result;
    }

    public List<User> randomUsers() throws ServerInternalException, NotStartException {
        List<User> result = new ArrayList<>();
        List<String> list = randomOpenIds(getUserSize());
        for (String s : list) {
            User user = (User) getRedisTemplate().opsForValue().get(s);
            result.add(user);
        }
        return result;
    }

    //TODO 后端判断抽奖重复
    //TODO 签到的跟抽奖的分离出来，签到以五份数据保存，抽奖保存在一份，五个key->value
    //抽奖
    public User LotterySelect() throws NotStartException, ServerInternalException {
        //1.获取所有签到人员的信息
        List<User> signUser = randomUsers();
        List<User> list = RandomUtil.createRandomList(signUser, 1);
        //分批次抽奖中奖名单
        //2.进行随机筛选出一条（抽奖）
        return list.get(0);
    }

    public List<User> randomUser(Integer count) throws NotStartException, ServerInternalException, LotteryException {
        //TODO 数量超过返回错误
        List<User> userList = new ArrayList<>();
        Long luckySetSize = getLuckySetSize();
        Long userSize = getUserSize();
        long l = userSize - luckySetSize;
        if (count > l) {
            throw new LotteryException(String.valueOf(l));
        }
        if (luckySetSize.equals(userSize)) {
            //TODO REVIEW
            throw new ServerInternalException("中奖池已满");
        }
        List<String> randomOpenIds = this.randomOpenIds(Long.valueOf(count));
        for (int i = 0; i < randomOpenIds.size(); i++) {
            String openId = randomOpenIds.get(i);
            long inLuckySet = saveInLuckySet(openId);
            //存入中奖池
            if (inLuckySet == 0) {
                //已经获奖,重新抽
                List<String> openIds = this.randomOpenIds(1L);
                randomOpenIds.remove(openId);
                randomOpenIds.add(openIds.get(0));
                i = i - 1;
                //移除-1，当前位置被占用
            } else if (inLuckySet == 1) {
                User value = (User) getRedisTemplate().opsForValue().get(openId);
                userList.add(value);
            }
        }
        return userList;
    }

    private Long saveInLuckySet(String openId) throws NotStartException, ServerInternalException {
        Long aLong = getRedisTemplate().opsForSet().add(CommonConstant.LUCKY_USER, openId);
        if (aLong == 1) {
            //success
            return aLong;
        } else if (aLong == 0) {
            //fail
            return aLong;
        } else {
            throw new ServerInternalException("失败");
        }
    }

    public Set<String> getLuckyOpenIdSet() throws ServerInternalException, NotStartException {
        Set<String> set = (Set) getRedisTemplate().opsForSet().members(CommonConstant.LUCKY_USER);
        return set;
    }

    public List<User> getLuckySet() throws NotStartException, ServerInternalException {
        Set<String> set = this.getLuckyOpenIdSet();
        List<User> userList = new ArrayList<>();
        for (String openId : set) {
            User user = (User) getRedisTemplate().opsForValue().get(openId);
            userList.add(user);
        }
        return userList;
    }

    public Long getLuckySetSize() throws NotStartException, ServerInternalException {
        Long setSize = getRedisTemplate().opsForSet().size(CommonConstant.LUCKY_USER);
        return setSize;
    }

    public UserListCount orderMealUserCountAndUserList() throws ServerInternalException, NotStartException {
        UserListCount userListCount = new UserListCount();
        Set<String> openIdSet = getOpenIdSet();
        List<User> userList = new ArrayList<>();
        int orderCount = 0;
        if (null != openIdSet) {
            for (String next : openIdSet) {
                User user = get(next);
                userList.add(user);
                if (Integer.valueOf(1).equals(user.getOrder())) {
                    orderCount++;
                }
            }
        }
        userListCount.setNow(MyDateUtil.moment());
        userListCount.setOrderCount(orderCount);
        userListCount.setUsers(userList);
        return userListCount;
    }

    public UserListCount orderMealUserCountAndUserList(int index) throws ServerInternalException, NotStartException {
        UserListCount userListCount = new UserListCount();
        Set<String> openIdSet = null;
        try {
            openIdSet = getOpenIdSet(index);
        } catch (Exception e) {
            log.error("error", e);
        }
        List<User> userList = new ArrayList<>();
        int orderCount = 0;
        if (null != openIdSet) {
            for (String next : openIdSet) {
                User user = get(index, next);
                userList.add(user);
                if (Integer.valueOf(1).equals(user.getOrder())) {
                    orderCount++;
                }
            }
        }
        userListCount.setNow(MyDateUtil.moment());
        userListCount.setOrderCount(orderCount);
        userListCount.setUsers(userList);
        return userListCount;
    }

    public List<User> orderMealUserList() throws ServerInternalException, NotStartException {
        Set<String> openIdSet = getOpenIdSet();
        List<User> userList = new ArrayList<>();
        if (null != openIdSet) {
            for (String next : openIdSet) {
                User user = get(next);
                if (user.getOrder().equals(1)) {
                    userList.add(user);
                }
            }
        }
        return userList;
    }

    public Integer saveMessage(String message) throws ServerInternalException, NotStartException {
        Message msg = new Message();
        msg.setMessage(message);
        msg.setCreateTime(MyDateUtil.moment());
        Long aLong = getRedisTemplate().opsForSet().add(CommonConstant.MESSAGE, msg);
        if (aLong == 1) {
            return 1;
        } else if (aLong == 0) {
            return 0;
        }
        return null;
    }

    public Integer saveMessage(LuckyUser luckyUser, String message) throws ServerInternalException, NotStartException {
        Set<String> luckyOpenIdSet = this.getLuckyOpenIdSet();
        if (luckyOpenIdSet.contains(luckyUser.getOpenId())) {
            User user = this.get(luckyUser.getOpenId());
            LuckyUserMessage luckyUserMessage = new LuckyUserMessage();
            luckyUserMessage.setOpenId(luckyUser.getOpenId());
            luckyUserMessage.setName(luckyUser.getName());
            luckyUserMessage.setDegree(luckyUser.getDegree());
            luckyUserMessage.setDepName(user.getDepName());
            luckyUserMessage.setSignDate(user.getSignDate());
            luckyUserMessage.setMessage(message);
            luckyUserMessage.setNickName(user.getNickName());
            Long aLong = getRedisTemplate().opsForSet().add(CommonConstant.MESSAGE, luckyUserMessage);
            if (aLong == 1) {
                return 1;
            } else if (aLong == 0) {
                return 0;
            }
        } else {
            throw new ServerInternalException("非法消息，该用户不存在中奖池中，openId=" + luckyUser.getOpenId() + ",name=" + luckyUser.getName());
        }
        return null;
    }

    public Set<LuckyUserMessage> getAllMessage() throws ServerInternalException, NotStartException {
        return (Set) getRedisTemplate().opsForSet().members(CommonConstant.MESSAGE);
    }

    public List<LuckyUserMessage> getMessageByDegree(Integer degree) throws NotStartException, ServerInternalException {
        Set<LuckyUserMessage> allMessage = this.getAllMessage();
        ArrayList<LuckyUserMessage> luckyUserMessageList = new ArrayList<>();
        for (LuckyUserMessage lucky : allMessage) {
            if (degree.equals(lucky.getDegree())) {
                luckyUserMessageList.add(lucky);
            }
        }
        return luckyUserMessageList;
    }
}
