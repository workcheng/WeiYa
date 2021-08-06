package com.workcheng.weiya.controller;

import com.workcheng.weiya.common.config.WeiYaConfig;
import com.workcheng.weiya.common.constant.ErrorCode;
import com.workcheng.weiya.common.domain.UnionUser;
import com.workcheng.weiya.common.domain.User;
import com.workcheng.weiya.common.dto.UserDto;
import com.workcheng.weiya.common.exception.*;
import com.workcheng.weiya.common.exception.LotteryException;
import com.workcheng.weiya.common.exception.NotStartException;
import com.workcheng.weiya.common.exception.VoteException;
import com.workcheng.weiya.common.utils.ImageUtil;
import com.workcheng.weiya.common.utils.ResponseUtil;
import com.workcheng.weiya.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenghui on 2016/12/20.
 */
@RequestMapping("user")
@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final StringRedisTemplate stringRedisTemplate;
    private final WeiYaConfig weiYaConfig;

    /**
     * 保存签到信息
     *
     * @param u
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Object save(@RequestBody @Valid User u, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseUtil.failure(bindingResult.getFieldError().getDefaultMessage());
        }
        if (u.getName().length() > 30) {
            return ResponseUtil.failure("姓名不超过30字符");
        }
        if (StringUtils.isBlank(u.getOpenId())) {
            return ResponseUtil.failure();
        }
        try {
            userService.save(u);
        } catch (HasSignException e) {
            log.error("error", e);
            return ResponseUtil.failure(ErrorCode.HAS_SIGN);
        } catch (ServerInternalException e) {
            log.error("error", e);
            return ResponseUtil.failure(e.getMessage());
        } catch (VoteException e) {
            log.error("error", e);
            return ResponseUtil.failure(ErrorCode.ERROR_VOTE);
        } catch (NotStartException e) {
            log.error("error", e);
            return ResponseUtil.failure(ErrorCode.NOT_START, weiYaConfig.getWeiyaTime());
        } catch (WxErrorException e) {
            log.error("error", e);
            return ResponseUtil.failure(e);
        }
        return ResponseUtil.failure(ErrorCode.HAS_SIGN);
    }

    /**
     * 获取签到信息
     *
     * @param openId
     * @return
     */
    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    public Object get(@RequestParam(value = "id") String openId) {
        try {
            return ResponseUtil.success(userService.get(openId));
        } catch (NotStartException e) {
            log.error("error", e);
            return ResponseUtil.failure(ErrorCode.NOT_START, weiYaConfig.getWeiyaTime());
        } catch (ServerInternalException e) {
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 判断是否签到
     *
     * @param openId
     * @return
     */
    @RequestMapping(value = "/isMember", method = RequestMethod.GET)
    public Object isMember(@RequestParam(value = "id") String openId) {
        try {
            if (!weiYaConfig.signTime()) {
                log.info("签到时间为：{} ~ {}", weiYaConfig.getSinOpenTime(), weiYaConfig.getSignCloseTime());
                return ResponseUtil.failure(ErrorCode.NOT_START, weiYaConfig.getSignCloseTime());
            }
            if (userService.isMember(openId)) {
                final User user = userService.get(openId);
                UserDto userDto = new UserDto();
                BeanUtils.copyProperties(user, userDto);
                return ResponseUtil.success(ErrorCode.HAS_SIGN, userDto);
            } else {
                return ResponseUtil.success(ErrorCode.NOT_SIGN);
            }
        } catch (NotStartException e) {
            log.error("error", e);
            return ResponseUtil.failure(ErrorCode.NOT_START, weiYaConfig.getWeiyaTime());
        } catch (ServerInternalException e) {
            log.error("error", e);
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 获取签到用户信息
     *
     * @return
     */
    @RequestMapping(value = "/userList", method = RequestMethod.GET)
    public Object getRandomUserList(Integer count) {
        try {
            if (null == count) {
                count = 100;
            }
            return ResponseUtil.success(userService.randomUsers(count));
        } catch (NotStartException e) {
            log.error("error", e);
            return ResponseUtil.failure(ErrorCode.NOT_START);
        } catch (ServerInternalException e) {
            return ResponseUtil.failure(e);
        }
    }

    @Deprecated
    @RequestMapping(value = "/submitLottery", method = RequestMethod.POST)
    public Object confirmLucky(@RequestBody List<String> openIds) {
        List<UnionUser> onlyUsers = new ArrayList<>();
        try {
            for (String openId : openIds) {
                UnionUser onlyUser = userService.get(openId);
                onlyUsers.add(onlyUser);
            }
            userService.commitLotteryPerson(onlyUsers);
        } catch (Exception e) {
            return ResponseUtil.failure(ErrorCode.ERROR);
        }
        return ResponseUtil.success(ErrorCode.SUCCESS);
    }

    /**
     * 抽奖，返回一个中奖者信息
     *
     * @return
     */
    @RequestMapping(value = "/lotterySelect", method = RequestMethod.GET)
    public Object lotterySelect() {
        try {
            return ResponseUtil.success(userService.randomUser(1));
        } catch (NotStartException e) {
            log.error("error", e);
            return ResponseUtil.failure(ErrorCode.NOT_START, weiYaConfig.getWeiyaTime());
        } catch (ServerInternalException e) {
            log.error("error", e);
            return ResponseUtil.failure(e.getMessage());
        } catch (LotteryException e) {
            log.error("error", e);
            return ResponseUtil.failure(ErrorCode.ERROR_LOTTERY, e.getMessage());
        }
    }

    /**
     * 抽奖，返回多个中奖者信息
     *
     * @param count
     * @return
     */
    @RequestMapping(value = "/lotteryUserList", method = RequestMethod.GET)
    public Object LotteryUser(@RequestParam Integer count) {
        try {
            return ResponseUtil.success(userService.randomUser(count));
        } catch (NotStartException e) {
            log.error("error", e);
            return ResponseUtil.failure(ErrorCode.NOT_START, weiYaConfig.getWeiyaTime());
        } catch (ServerInternalException e) {
            log.error("error", e);
            return ResponseUtil.failure(e.getMessage());
        } catch (LotteryException e) {
            log.error("error", e);
            return ResponseUtil.failure(ErrorCode.ERROR_LOTTERY, e.getMessage());
        }
    }

    /**
     * @param index 22号上午index=0，下午index=1；23号上午index=2,下午index=3，晚上index=4
     * @return
     */
    @RequestMapping(value = "/allUserList", method = RequestMethod.GET)
    public Object getUserList(Integer index) {
        try {
            if (null != index) {
                if (index > 4) {
                    log.error("列表不存在");
                    return ResponseUtil.failure("列表不存在");
                }
                return ResponseUtil.success(userService.orderMealUserCountAndUserList());
            }
            return ResponseUtil.success(userService.orderMealUserCountAndUserList());
        } catch (NotStartException e) {
            log.error("error", e);
            return ResponseUtil.failure(ErrorCode.NOT_START, weiYaConfig.getWeiyaTime());
        } catch (ServerInternalException e) {
            log.error("error", e);
            return ResponseUtil.failure(e);
        }
    }

    /**
     * 返回签到者数量
     *
     * @return
     */
    @RequestMapping(value = "/userListCount", method = RequestMethod.GET)
    public Object getUserListCount() {
        try {
            return ResponseUtil.success(userService.getUserSize());
        } catch (NotStartException e) {
            log.error("error", e);
            return ResponseUtil.failure(ErrorCode.NOT_START, weiYaConfig.getWeiyaTime());
        } catch (ServerInternalException e) {
            log.error("error", e);
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 获取中奖名单
     *
     * @param degree 0：一等奖；1：二等奖；2：三等奖 -1:所有
     * @return
     */
    @RequestMapping(value = "/luckyUserList", method = RequestMethod.GET)
    public Object getLuckyUserList(Integer degree) {
        try {
            if (null != degree) {
                if (degree == -1) {
                    return ResponseUtil.success(userService.getAllMessage());
                } else {
                    return ResponseUtil.success(userService.getMessageByDegree(degree));
                }
            }
            return ResponseUtil.success(userService.getAllMessage());
        } catch (ServerInternalException e) {
            log.error("error", e);
            return ResponseUtil.failure(e.getMessage());
        } catch (NotStartException e) {
            log.error("error", e);
            return ResponseUtil.failure(ErrorCode.NOT_START, weiYaConfig.getWeiyaTime());
        }
    }

    /**
     * 返回头像image
     *
     * @param url
     * @param response
     */
    @RequestMapping(value = "/headImgUrl", method = RequestMethod.GET)
    public void getHeadImgUrl(@RequestParam String url, HttpServletResponse response) {
        // TODO 前端自己画圆角头像，不需要再通过服务端进行处理
        response.setContentType("image/jpeg");
        System.setProperty("java.awt.headless", "true");
        if (StringUtils.isBlank(url)) {
            return;
        }
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ImageUtil.toPNG(new URL(url), outputStream, 250, 250, stringRedisTemplate);
        } catch (IOException e) {
            log.error("error", e);
        }
    }

    /**
     * 获取未中奖者的数量
     *
     * @return
     */
    @RequestMapping(value = "/unHitUserSize", method = RequestMethod.GET)
    public Object getNotLuckyUserSize() {
        try {
            Long userSize = userService.getUserSize();
            Long luckySetSize = userService.getLuckySetSize();
            long l = userSize - luckySetSize;
            return ResponseUtil.success(l);
        } catch (NotStartException e) {
            log.error("error", e);
            return ResponseUtil.failure(ErrorCode.NOT_START, weiYaConfig.getWeiyaTime());
        } catch (ServerInternalException e) {
            log.error("error", e);
            return ResponseUtil.failure(e.getMessage());
        }
    }
}
