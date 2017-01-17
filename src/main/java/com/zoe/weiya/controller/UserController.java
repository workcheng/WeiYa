package com.zoe.weiya.controller;

import com.zoe.weiya.comm.constant.ZoeErrorCode;
import com.zoe.weiya.comm.exception.HasSignException;
import com.zoe.weiya.comm.exception.InternalException;
import com.zoe.weiya.comm.exception.NotStartException;
import com.zoe.weiya.comm.exception.VoteException;
import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.comm.response.ZoeObject;
import com.zoe.weiya.model.OnlyUser;
import com.zoe.weiya.model.User;
import com.zoe.weiya.service.user.UserService;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenghui on 2016/12/20.
 */
@RequestMapping("user")
@RestController
public class UserController {
    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 保存签到信息
     *
     * @param u
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public Object save(@RequestBody @Valid User u) {
        if (StringUtils.isBlank(u.getOpenId())) {
            return ZoeObject.failure();
        }
        try {
            userService.save(u);
        } catch (HasSignException e) {
            log.error("error", e);
            return ZoeObject.failure(ZoeErrorCode.HAS_SIGN);
        } catch (InternalException e) {
            log.error("error", e);
            return ZoeObject.failure(ZoeErrorCode.ERROR_INTERNAL);
        } catch (VoteException e) {
            log.error("error", e);
            return ZoeObject.failure(ZoeErrorCode.ERROR_VOTE);
        } catch (NotStartException e){
            log.error("error", e);
            return ZoeObject.failure(ZoeErrorCode.NOT_START);
        } catch (WxErrorException e) {
            log.error("error", e);
            return ZoeObject.failure(e);
        }
        return ZoeObject.failure(ZoeErrorCode.HAS_SIGN);
    }

    /**
     * 获取签到信息
     *
     * @param openId
     * @return
     */
    @RequestMapping(value = "getUser", method = RequestMethod.GET)
    public Object get(@RequestParam(value = "id") String openId) {
        try {
            return ZoeObject.success(userService.get(openId));
        } catch (NotStartException e) {
            log.error("error", e);
            return ZoeObject.failure(ZoeErrorCode.NOT_START);
        } catch (InternalException e) {
            return ZoeObject.failure(e);
        }
    }

    /**
     * 判断是否签到
     *
     * @param openId
     * @return
     */
    @RequestMapping(value = "isMember", method = RequestMethod.GET)
    public Object isMember(@RequestParam(value = "id") String openId) {
        try {
            if (userService.isMember(openId)) {
                return ZoeObject.success(ZoeErrorCode.HAS_SIGN);
            } else {
                return ZoeObject.success(ZoeErrorCode.NOT_SIGN);
            }
        } catch (NotStartException e) {
            log.error("error", e);
            return ZoeObject.failure(ZoeErrorCode.NOT_START);
        } catch (InternalException e) {
            log.error("error", e);
            return ZoeObject.failure(e);
        }
    }

    /**
     * 获取签到用户信息
     *
     * @return
     */
    @RequestMapping(value = "userList", method = RequestMethod.GET)
    public Object getRandomUserList(Integer count) {
        try {
            if(null == count){
                count = 100;
            }
            return ZoeObject.success(userService.randomUsers(count));
        } catch (NotStartException e) {
            log.error("error", e);
            return ZoeObject.failure(ZoeErrorCode.NOT_START);
        } catch (InternalException e) {
            return ZoeObject.failure(e);
        }
    }

    @RequestMapping(value = "submitLottery", method = RequestMethod.POST)
    public Object confirmLucky(@RequestBody List<String> openIds) {
        List<OnlyUser> onlyUsers = new ArrayList<>();
        try {
            for (String openId : openIds) {
                OnlyUser onlyUser = userService.get(openId);
                onlyUsers.add(onlyUser);
            }
            userService.commitLotteryPerson(onlyUsers);
        } catch (Exception e) {
            return ZoeObject.failure(ZoeErrorCode.ERROR);
        }
        return ZoeObject.success(ZoeErrorCode.SUCCESS);
    }

    @RequestMapping(value = "lotterySelect", method = RequestMethod.GET)
    public Object LotterySelect() {
        try {
            return ZoeObject.success(userService.randomUser(1));
        } catch (NotStartException e) {
            log.error("error", e);
            return ZoeObject.failure(ZoeErrorCode.NOT_START);
        } catch (InternalException e){
            log.error("error", e);
            return ZoeObject.failure(ZoeErrorCode.ERROR);
        }
    }

    @RequestMapping(value = "lotteryUserList", method = RequestMethod.GET)
    public Object LotteryUser(@RequestParam Integer count) {
        try {
            return ZoeObject.success(userService.randomUser(count));
        } catch (NotStartException e) {
            log.error("error", e);
            return ZoeObject.failure(ZoeErrorCode.NOT_START);
        } catch (InternalException e){
            log.error("error", e);
            return ZoeObject.failure(ZoeErrorCode.ERROR);
        }
    }

    /**
     *
     * @param index 22号上午index=0，下午index=1；23号上午index=2,下午index=3，晚上index=4
     * @return
     */
    @RequestMapping(value = "allUserList", method = RequestMethod.GET)
    public Object getUserList(Integer index) {
        try {
            if(null != index){
                if(index > 4){
                    log.error("列表不存在");
                    return ZoeObject.failure("列表不存在");
                }
                return ZoeObject.success(userService.orderMealUserCountAndUserList(index));
            }
            return ZoeObject.success(userService.orderMealUserCountAndUserList());
        } catch (NotStartException e) {
            log.error("error", e);
            return ZoeObject.failure(ZoeErrorCode.NOT_START);
        } catch (InternalException e) {
            log.error("error", e);
            return ZoeObject.failure(e);
        }
    }

    @RequestMapping(value = "userListCount", method = RequestMethod.GET)
    public Object getUserListCount() {
        try {
            return ZoeObject.success(userService.getUserSize());
        } catch (NotStartException e) {
            log.error("error", e);
            return ZoeObject.failure(ZoeErrorCode.NOT_START);
        } catch (InternalException e) {
            log.error("error", e);
            return ZoeObject.failure(e);
        }
    }

    /**
     * 获取中奖名单
     * @param degree 0：一等奖；1：二等奖；2：三等奖
     * @return
     */
    @RequestMapping(value = "luckyUserList", method = RequestMethod.GET)
    public Object getLuckyUserList(Integer degree){
        try {
            if(null != degree){
                return ZoeObject.success(userService.getMessageByDegree(degree));
            }
            return ZoeObject.success(userService.getAllMessage());
        } catch (InternalException e) {
            log.error("error", e);
            return ZoeObject.failure(e);
        } catch (NotStartException e) {
            log.error("error", e);
            return ZoeObject.failure(ZoeErrorCode.NOT_START);
        }
    }
}
