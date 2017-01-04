package com.zoe.weiya.service.user;

import com.zoe.weiya.comm.constant.ZoeErrorCode;
import com.zoe.weiya.comm.exception.HasSignException;
import com.zoe.weiya.comm.exception.InternalException;
import com.zoe.weiya.comm.exception.NotStartException;
import com.zoe.weiya.comm.exception.VoteException;
import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.comm.properties.ZoeProperties;
import com.zoe.weiya.comm.redis.ZoeRedisTemplate;
import com.zoe.weiya.comm.response.ResponseMsg;
import com.zoe.weiya.comm.response.ZoeObject;
import com.zoe.weiya.model.OnlyUser;
import com.zoe.weiya.model.User;
import com.zoe.weiya.model.ZoeDate;
import com.zoe.weiya.util.RandomUtil;
import com.zoe.weiya.util.ZoeDateUtil;
import me.chanjar.weixin.common.bean.result.WxError;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by andy on 2016/12/20.
 */
@Service
public class UserService {
    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(UserService.class);
    private static final String SIGN_USER_FIRST_DAY_MORNING = "0";
    private static final String SIGN_USER_FIRST_DAY_NOON = "1";
    private static final String SIGN_USER_SECOND_DAY_MORNING = "2";
    private static final String SIGN_USER_SECOND_DAY_NOON = "3";
    private static final String SIGN_USER_SECOND_DAY_NIGHT = "4";
    private static final String LOTTERY = "lottery";
    private static final String USER = "USER";
    private static final String MORNING = "MORNING";
    private static final String NOON = "NOON";
    private static final String NIGHT = "NIGHT";
    private static final String FIRST_DAY = "FIRST";
    private static final String SECOND_DAY = "SECOND";
    private static final String ONE_SET= "SET1";
    private static final String TWO_SET= "SET2";
    private static final String THREE_SET= "SET3";
    private static final String FOUR_SET= "SET4";
    private static final String FIVE_SET= "SET5";
    private static final String SET= "SET";

    @Qualifier("zoeRedisTemplate0")
    @Autowired private ZoeRedisTemplate zoeRedisTemplate0;
    @Qualifier("zoeRedisTemplate1")
    @Autowired private ZoeRedisTemplate zoeRedisTemplate1;
    @Qualifier("zoeRedisTemplate2")
    @Autowired private ZoeRedisTemplate zoeRedisTemplate2;
    @Qualifier("zoeRedisTemplate3")
    @Autowired private ZoeRedisTemplate zoeRedisTemplate3;
    @Qualifier("zoeRedisTemplate4")
    @Autowired private ZoeRedisTemplate zoeRedisTemplate4;
    @Autowired
    private WxMpServiceImpl wxMpService;

    private ZoeRedisTemplate getZoeRedisTemplate() throws NotStartException {
        ZoeRedisTemplate[] zoeRedisTemplateIndexList = {
            zoeRedisTemplate0,zoeRedisTemplate1,zoeRedisTemplate2,zoeRedisTemplate3,zoeRedisTemplate4};
            return zoeRedisTemplateIndexList[Integer.valueOf(getIndex())];
    }

    public void save(User u) throws NotStartException, InternalException, HasSignException, WxErrorException, VoteException {
        if(StringUtils.isEmpty(u.getOpenId())){
            throw new InternalException("openid不能为空");
        }
        try {
            WxMpUser wxMpUser = wxMpService.getUserService().userInfo(u.getOpenId());
            u.setNickName(wxMpUser.getNickname());
        } catch (WxErrorException e) {
            log.error("error",e);
            WxError error = e.getError();
            if(null != error){
                throw new WxErrorException(error);
            }
        }
        Long add = this.getZoeRedisTemplate().getSetOperations().add(UserService.USER, u.getOpenId());
        if(add == 0){
            throw new HasSignException("已经签到");
        }
        this.getZoeRedisTemplate().setValue(u.getOpenId(), u);
    }

    @Deprecated
    public void save0(User u) throws HasSignException, InternalException, WxErrorException, NotStartException {
        WxMpUser wxMpUser = wxMpService.getUserService().userInfo(u.getOpenId());
        if(null != wxMpUser){
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

    public void commitLotteryPerson(List<OnlyUser> users) throws NotStartException {
        SimpleDateFormat sdf = new SimpleDateFormat();
        String format = sdf.format(new Date());
        getZoeRedisTemplate().setValue(format,users);
    }

    public ResponseMsg deleteAll(List<OnlyUser> users) throws NotStartException {
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

    public User get(String openId) throws NotStartException {
        return (User) getZoeRedisTemplate().getValue(openId);
    }

    public Long saveInSet(String openId) throws NotStartException {
        return getZoeRedisTemplate().setSet(USER, openId);
    }

    public Set<String> getOpenIdSet() throws NotStartException {
        return (Set) getZoeRedisTemplate().getSet(USER);
    }

    public boolean isMember(String openId) throws NotStartException {
        return getZoeRedisTemplate().isMember(USER, openId);
    }

    public Long getUserSize() throws NotStartException {
        return getZoeRedisTemplate().getSetSize(USER);
    }

    public List<User> getSignUser() throws NotStartException {
        Set<String> openIdSet = this.getOpenIdSet();
        List<User> list = new ArrayList<>();
        if (null != openIdSet) {
            Iterator<String> i = openIdSet.iterator();//迭代
            while (i.hasNext()) {//遍历
                String openId = i.next();
                User user = (User) getZoeRedisTemplate().getValue(openId);
                list.add(user);
                
            }
        }
        return list;
    }

    public Set<User> getSignUserSet() throws NotStartException {
        Set<String> idSet = this.getOpenIdSet();
        Set<User> list = new HashSet<>();
        if (null != idSet) {
            Iterator<String> i = idSet.iterator();
            while (i.hasNext()) {
                String openId = i.next();
                User user = (User) getZoeRedisTemplate().getValue(openId);
                list.add(user);
            }
        }
        return list;
    }
    //TODO 后端判断抽奖重复
    //TODO 签到的跟抽奖的分离出来，签到以五份数据保存，抽奖保存在一份，五个key->value
    //抽奖
    public User LotterySelect() throws NotStartException {
        //1.获取所有签到人员的信息
        List<User> signUser = getSignUser();
        List<User> list = RandomUtil.createRandomList(signUser, 1);
        //分批次抽奖中奖名单
        //2.进行随机筛选出一条（抽奖）
        User user = list.get(0);
        return user;
    }

    private String getTime(ZoeDate now){
        if( now.getHour() <= 12){
            return UserService.MORNING;
        }else if(now.getHour() > 12 && now.getHour() <= 18){
            return UserService.NOON;
        }else if(now.getHour() > 18){
            return UserService.NIGHT;
        }
        return null;
    }

    private String whichDay(ZoeDate now){
        ZoeDate startTime = ZoeProperties.getStartTime();
        String whichDay[] = {UserService.FIRST_DAY,UserService.SECOND_DAY};
        return whichDay[now.getDay()-startTime.getDay()];
    }

    private String getIndex() throws NotStartException{
        //TODO 考虑跨年跨月的情况
        ZoeDate now = ZoeDateUtil.moment();
        ZoeDate startTime = ZoeProperties.getStartTime();
        if(now.getYear().equals(startTime.getYear())){
            if(now.getMonth() >= startTime.getMonth()){
                if(now.getDay() >= startTime.getDay()){
                    if(UserService.FIRST_DAY.equals(whichDay(now))){
                        switch (getTime(now)){
                            case UserService.MORNING:
                                return UserService.SIGN_USER_FIRST_DAY_MORNING;
                            case UserService.NOON:
                                return UserService.SIGN_USER_FIRST_DAY_NOON;
                            default:
                                throw new NotStartException(ZoeErrorCode.NOT_START.getDescription());
                        }
                    }else if(UserService.SECOND_DAY.equals(whichDay(now))){
                        switch (getTime(now)){
                            case UserService.MORNING:
                                return UserService.SIGN_USER_SECOND_DAY_MORNING;
                            case UserService.NOON:
                                return UserService.SIGN_USER_SECOND_DAY_NOON;
                            case UserService.NIGHT:
                                return UserService.SIGN_USER_SECOND_DAY_NIGHT;
                            default:
                                throw new NotStartException(ZoeErrorCode.NOT_START.getDescription());
                        }
                    }

                }
            }
        }
        throw new NotStartException(ZoeErrorCode.NOT_START.getDescription());
    }

    private String getSetIndex() throws NotStartException {
        return UserService.SET+getIndex();
    }

}
