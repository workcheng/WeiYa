package com.workcheng.weiya.controller;

import com.workcheng.weiya.common.constant.CommonConstant;
import com.workcheng.weiya.common.constant.ErrorCode;
import com.workcheng.weiya.common.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by andy on 2016/12/18.
 */
@RequestMapping("/sign")
@RestController
@RequiredArgsConstructor
@Slf4j
public class SignController {
    private final WxMpServiceImpl wxMpService;

    /**
     * 构建js-sdk所需配置
     *
     * @param url
     * @return
     */
    @RequestMapping(value = "/url", method = RequestMethod.GET)
    public Object getSign(@RequestParam String url) {
        try {
            return ResponseUtil.success(wxMpService.createJsapiSignature(url));
        } catch (WxErrorException e) {
            log.error(CommonConstant.ERROR + e.toString());
            return ResponseUtil.failure(ErrorCode.ERROR_WECHAT);
        }
    }

    /**
     * 根据openId获取用户信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public Object getUserInfo(@RequestParam String id) {
        try {
            return ResponseUtil.success(wxMpService.getUserService().userInfo(id));
        } catch (WxErrorException e) {
            log.error("error", e);
            return ResponseUtil.failure(ErrorCode.ERROR_OPENID);
        }
    }
}
