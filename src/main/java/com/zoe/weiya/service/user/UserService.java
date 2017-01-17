package com.zoe.weiya.service.user;

import com.zoe.weiya.comm.constant.CommonConstant;
import com.zoe.weiya.comm.constant.ZoeErrorCode;
import com.zoe.weiya.comm.exception.HasSignException;
import com.zoe.weiya.comm.exception.InternalException;
import com.zoe.weiya.comm.exception.NotStartException;
import com.zoe.weiya.comm.exception.VoteException;
import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.comm.redis.ZoeRedisTemplate;
import com.zoe.weiya.comm.response.ResponseMsg;
import com.zoe.weiya.comm.response.ZoeObject;
import com.zoe.weiya.model.*;
import com.zoe.weiya.model.responseModel.UserListCount;
import com.zoe.weiya.util.RandomUtil;
import com.zoe.weiya.util.ZoeDateUtil;
import com.zoe.weiya.util.ZoeUtil;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by andy on 2016/12/20.
 */
@Service
public class UserService {
    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(UserService.class);
    @Qualifier("zoeRedisTemplate0")
    @Autowired
    private ZoeRedisTemplate zoeRedisTemplate0;
    @Qualifier("zoeRedisTemplate1")
    @Autowired
    private ZoeRedisTemplate zoeRedisTemplate1;
    @Qualifier("zoeRedisTemplate2")
    @Autowired
    private ZoeRedisTemplate zoeRedisTemplate2;
    @Qualifier("zoeRedisTemplate3")
    @Autowired
    private ZoeRedisTemplate zoeRedisTemplate3;
    @Qualifier("zoeRedisTemplate4")
    @Autowired
    private ZoeRedisTemplate zoeRedisTemplate4;
    @Autowired
    private WxMpServiceImpl wxMpService;
    private List<ZoeRedisTemplate> zoeRedisTemplateIndexList;


    @PostConstruct
    private void init() {
        zoeRedisTemplateIndexList = new ArrayList<ZoeRedisTemplate>() {
            private static final long serialVersionUID = 1L;
            {
                add(zoeRedisTemplate0);
                add(zoeRedisTemplate1);
                add(zoeRedisTemplate2);
                add(zoeRedisTemplate3);
                add(zoeRedisTemplate4);
            }
        };
    }

    private ZoeRedisTemplate getZoeRedisTemplate() throws NotStartException, InternalException {
        try {
            return zoeRedisTemplateIndexList.get(Integer.valueOf(ZoeUtil.getIndex()));
        } catch (IndexOutOfBoundsException e) {
            throw new InternalException("时间设置不对");
        }
    }

    public void save(User u) throws NotStartException, InternalException, HasSignException, WxErrorException, VoteException {
        if (StringUtils.isEmpty(u.getOpenId())) {
            throw new InternalException("openid不能为空");
        }
        WxMpUser wxMpUser = wxMpService.getUserService().userInfo(u.getOpenId());
        u.setNickName(wxMpUser.getNickname());
        u.setHeadImgUrl(wxMpUser.getHeadImgUrl());
        Long add = this.getZoeRedisTemplate().getSetOperations().add(CommonConstant.USER, u.getOpenId());
        if (add == 0) {
            throw new HasSignException("已经签到");
        }
        this.getZoeRedisTemplate().setValue(u.getOpenId(), u);
    }

    @Deprecated
    public void save0(User u) throws HasSignException, InternalException, WxErrorException, NotStartException {
        WxMpUser wxMpUser = wxMpService.getUserService().userInfo(u.getOpenId());
        if (null != wxMpUser) {
            Long aLong = this.saveInSet(u.getOpenId());
            if (aLong == 1) {
                u.setNickName(wxMpUser.getNickname());
                getZoeRedisTemplate().setValue(u.getOpenId(), u);
            } else if (aLong == 0) {
                throw new HasSignException(ZoeErrorCode.HAS_SIGN.getDescription());
            } else {
                throw new InternalException(ZoeErrorCode.ERROR_INTERNAL.getDescription());
            }

        }
    }

    public void commitLotteryPerson(List<OnlyUser> users) throws NotStartException, InternalException {
        SimpleDateFormat sdf = new SimpleDateFormat();
        String format = sdf.format(new Date());
        getZoeRedisTemplate().setValue(format, users);
    }

    public ResponseMsg deleteAll(List<OnlyUser> users) throws NotStartException, InternalException {
        boolean flag = false;
        for (OnlyUser onlyUser : users) {
            getZoeRedisTemplate().deleteHash(onlyUser.getOpenId());
            flag = true;
        }
        if (flag) {
            return ZoeObject.failure(ZoeErrorCode.SUCCESS);
        } else {
            return ZoeObject.failure(ZoeErrorCode.ERROR);
        }
    }

    public User get(String openId) throws NotStartException, InternalException {
        return (User) getZoeRedisTemplate().getValue(openId);
    }

    public User get(int index, String openId) throws NotStartException, InternalException {
        return (User) zoeRedisTemplateIndexList.get(index).getValue(openId);
    }

    public Long saveInSet(String openId) throws NotStartException, InternalException {
        return getZoeRedisTemplate().setSet(CommonConstant.USER, openId);
    }

    public Set<String> getOpenIdSet() throws NotStartException, InternalException {
        return (Set) getZoeRedisTemplate().getSet(CommonConstant.USER);
    }

    public Set<String> getOpenIdSet(int index) throws NotStartException, InternalException {
        return (Set) zoeRedisTemplateIndexList.get(index).getSet(CommonConstant.USER);
    }

    public boolean isMember(String openId) throws NotStartException, InternalException {
        return getZoeRedisTemplate().isMember(CommonConstant.USER, openId);
    }

    public Long getUserSize() throws NotStartException, InternalException {
        return getZoeRedisTemplate().getSetSize(CommonConstant.USER);
    }

    public Set<String> randomOpenIdSet(Long count) throws InternalException, NotStartException {
        Set<String> set = new HashSet<String>();
        set.addAll(randomOpenIds(count));
        return set;
    }

    public List<String> randomOpenIds(Long count) throws InternalException, NotStartException {
        return (List) getZoeRedisTemplate().randomMember(CommonConstant.USER, count);
    }

    public List<User> randomUsers(Integer count) throws InternalException, NotStartException {
        List<User> result = new ArrayList<>();
        List<String> list = randomOpenIds(Long.valueOf(count));
        for (int i = 0; i < list.size(); i++) {
            User user = (User) getZoeRedisTemplate().getValue(list.get(i));
            result.add(user);
        }
        return result;
    }

    public List<User> randomUsers() throws InternalException, NotStartException {
        List<User> result = new ArrayList<>();
        List<String> list = randomOpenIds(getUserSize());
        for (int i = 0; i < list.size(); i++) {
            User user = (User) getZoeRedisTemplate().getValue(list.get(i));
            result.add(user);
        }
        return result;
    }

    //TODO 后端判断抽奖重复
    //TODO 签到的跟抽奖的分离出来，签到以五份数据保存，抽奖保存在一份，五个key->value
    //抽奖
    public User LotterySelect() throws NotStartException, InternalException {
        //1.获取所有签到人员的信息
        List<User> signUser = randomUsers();
        List<User> list = RandomUtil.createRandomList(signUser, 1);
        //分批次抽奖中奖名单
        //2.进行随机筛选出一条（抽奖）
        User user = list.get(0);
        return user;
    }

    public List<User> randomUser(Integer count) throws NotStartException, InternalException {
        //TODO 数量超过返回错误
        List<String> randomOpenIds = this.randomOpenIds(Long.valueOf(count));
        List<User> userList = new ArrayList<>();
        Long luckySetSize = getLuckySetSize();
        Long userSize = getUserSize();
        if (luckySetSize == userSize) {//TODO REVIEW
            throw new InternalException("中奖池已满");
        }
        for (int i = 0; i < randomOpenIds.size(); i++) {
            String openId = randomOpenIds.get(i);
            long inLuckySet = saveInLuckySet(openId);//存入中奖池
            if (inLuckySet == 0) {//已经获奖,重新抽
                List<String> openIds = this.randomOpenIds(Long.valueOf(1));
                randomOpenIds.remove(openId);
                randomOpenIds.add(openIds.get(0));
                i = i - 1;//移除-1，当前位置被占用
            } else if (inLuckySet == 1) {
                User value = (User) getZoeRedisTemplate().getValue(openId);
                userList.add(value);
            }
        }
        return userList;
    }

    private Long saveInLuckySet(String openId) throws NotStartException, InternalException {
        Long aLong = getZoeRedisTemplate().setSet(CommonConstant.LUCKY_USER, openId);
        if (aLong == 1) {
            //success
            return aLong;
        } else if (aLong == 0) {
            //fail
            return aLong;
        } else {
            throw new InternalException("失败");
        }
    }

    public Set<String> getLuckyOpenIdSet() throws InternalException, NotStartException {
        Set<String> set = (Set) getZoeRedisTemplate().getSet(CommonConstant.LUCKY_USER);
        return set;
    }

    public List<User> getLuckySet() throws NotStartException, InternalException {
        Set<String> set = this.getLuckyOpenIdSet();
        List<User> userList = new ArrayList<>();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String openId = iterator.next();
            User user = (User) getZoeRedisTemplate().getValue(openId);
            userList.add(user);
        }
        return userList;
    }

    public Long getLuckySetSize() throws NotStartException, InternalException {
        Long setSize = getZoeRedisTemplate().getSetSize(CommonConstant.LUCKY_USER);
        return setSize;
    }

    public UserListCount orderMealUserCountAndUserList() throws InternalException, NotStartException {
        UserListCount userListCount = new UserListCount();
        Set<String> openIdSet = getOpenIdSet();
        List<User> userList = new ArrayList<>();
        int orderCount = 0;
        if (null != openIdSet) {
            Iterator<String> iterator = openIdSet.iterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                User user = get(next);
                userList.add(user);
                if (Integer.valueOf(1).equals(user.getOrder())) {
                    orderCount++;
                }
            }
        }
        userListCount.setNow(ZoeDateUtil.moment());
        userListCount.setOrderCount(orderCount);
        userListCount.setUsers(userList);
        return userListCount;
    }

    public UserListCount orderMealUserCountAndUserList(int index) throws InternalException, NotStartException {
        UserListCount userListCount = new UserListCount();
        Set<String> openIdSet = getOpenIdSet(index);
        List<User> userList = new ArrayList<>();
        int orderCount = 0;
        if (null != openIdSet) {
            Iterator<String> iterator = openIdSet.iterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                User user = get(index, next);
                userList.add(user);
                if (Integer.valueOf(1).equals(user.getOrder())) {
                    orderCount++;
                }
            }
        }
        userListCount.setNow(ZoeDateUtil.moment());
        userListCount.setOrderCount(orderCount);
        userListCount.setUsers(userList);
        return userListCount;
    }

    public List<User> orderMealUserList() throws InternalException, NotStartException {
        Set<String> openIdSet = getOpenIdSet();
        List<User> userList = new ArrayList<>();
        if (null != openIdSet) {
            Iterator<String> iterator = openIdSet.iterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                User user = get(next);
                if (user.getOrder().equals(1)) {
                    userList.add(user);
                }
            }
        }
        return userList;
    }

    public Integer saveMessage(String message) throws InternalException, NotStartException {
        Message msg = new Message();
        msg.setMessage(message);
        msg.setCreateTime(ZoeDateUtil.moment());
        Long aLong = getZoeRedisTemplate().setSet(CommonConstant.MESSAGE, msg);
        if (aLong == 1) {
            return 1;
        } else if (aLong == 0) {
            return 0;
        }
        return null;
    }

    public Integer saveMessage(LuckyUser luckyUser, String message) throws InternalException, NotStartException {
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
            Long aLong = getZoeRedisTemplate().setSet(CommonConstant.MESSAGE, luckyUserMessage);
            if (aLong == 1) {
                return 1;
            } else if (aLong == 0) {
                return 0;
            }
        } else {
            throw new InternalException("非法消息，该用户不存在中奖池中，openId="+luckyUser.getOpenId()+",name="+luckyUser.getName());
        }
        return null;
    }

    public Set<LuckyUserMessage> getAllMessage() throws InternalException, NotStartException {
        return (Set) getZoeRedisTemplate().getSet(CommonConstant.MESSAGE);
    }

    public List<LuckyUserMessage> getMessageByDegree(Integer degree) throws NotStartException, InternalException {
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
