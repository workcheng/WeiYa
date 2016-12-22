package com.zoe.weiya.controller;

import com.zoe.weiya.comm.constant.ZoeErrorCode;
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
        return ZoeObject.success(userService.save(u));
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

    @RequestMapping(value = "userList", method = RequestMethod.GET)
    public Object getUserList() {
        return ZoeObject.success(userService.getSignUser());
    }

    @RequestMapping(value = "lotterySelect", method = RequestMethod.GET)
    public Object LotterySelect() {
        return ZoeObject.success(userService.LotterySelect());
    }

}
