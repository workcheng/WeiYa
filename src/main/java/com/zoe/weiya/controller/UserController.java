package com.zoe.weiya.controller;

import com.zoe.weiya.comm.constant.ZoeErrorCode;
import com.zoe.weiya.comm.exception.HasSignException;
import com.zoe.weiya.comm.exception.InternalException;
import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.comm.response.ZoeObject;
import com.zoe.weiya.model.OnlyUser;
import com.zoe.weiya.model.User;
import com.zoe.weiya.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
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
            log.error("ermsg", e);
            return ZoeObject.failure(ZoeErrorCode.HAS_SIGN);
        } catch (InternalException e) {
            log.error("ermsg", e);
            return ZoeObject.failure(ZoeErrorCode.ERROR_INTERNAL);
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
        return ZoeObject.success(userService.get(openId));
    }

    /**
     * 判断是否签到
     *
     * @param openId
     * @return
     */
    @RequestMapping(value = "isMember", method = RequestMethod.GET)
    public Object isMember(@RequestParam(value = "id") String openId) {
        if (userService.isMember(openId)) {
            return ZoeObject.success(ZoeErrorCode.HAS_SIGN);
        } else {
            return ZoeObject.success(ZoeErrorCode.NOT_SIGN);
        }
    }

    /**
     * 获取签到用户信息
     *
     * @return
     */
    @RequestMapping(value = "userList", method = RequestMethod.GET)
    public Object getUserList() {
        return ZoeObject.success(userService.getSignUser());
    }

    @RequestMapping(value = "confirmLucky", method = RequestMethod.POST)
    public Object confirmLucky(String[] openIds) {
        List<OnlyUser> onlyUsers = new ArrayList<>();
        try {
            List<String> stringList = Arrays.asList(openIds);
            for (String openId : stringList) {
                OnlyUser onlyUser = userService.get(openId);
                onlyUsers.add(onlyUser);
            }
            userService.commitLotteryPerson(onlyUsers);
        } catch (Exception e) {
            return ZoeObject.failure(ZoeErrorCode.ERROR);
        }
        return ZoeObject.success(ZoeErrorCode.SUCCESS);
    }

    @RequestMapping(value = "reLottery", method = RequestMethod.POST)
    public Object reLottery(String openIds) {
        String[] ids = openIds.split(",");
        List<OnlyUser> onlyUsers = new ArrayList<>();
        try {
            List<String> stringList = Arrays.asList(ids);
            for (String openId : stringList) {
                OnlyUser onlyUser = userService.get(openId);
                onlyUsers.add(onlyUser);
            }
            userService.resetIsLuckyFlag(onlyUsers);
        } catch (Exception e) {
            return ZoeObject.failure(ZoeErrorCode.ERROR);
        }
        return ZoeObject.success(ZoeErrorCode.SUCCESS);
    }


    @RequestMapping(value = "lotterySelect", method = RequestMethod.GET)
    public Object LotterySelect() {
        return ZoeObject.success(userService.LotterySelect());
    }

}
