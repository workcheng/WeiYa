package com.workcheng.weiya.controller;


import com.workcheng.weiya.common.constant.CommonConstant;
import com.workcheng.weiya.common.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author andy
 * @date 2016/12/16
 */
@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final WxMpServiceImpl wxMpService;

    /**
     * 获取授权登陆的URL
     *
     * @param url
     * @param state
     * @return
     */
    @RequestMapping(value = "/url", method = RequestMethod.GET)
    public Object getAuthUrl(String url, String state) {
        return ResponseUtil.success(wxMpService.getOAuth2Service().buildAuthorizationUrl(url, CommonConstant.AUTH_USERINFO, state));
    }

    /**
     * 根据code获取access_token
     *
     * @param code
     * @return
     */
    @RequestMapping("/access_token")
    public Object getAccessToken(@RequestParam(value = "code") String code) {
        try {
            return ResponseUtil.success(wxMpService.getOAuth2Service().getAccessToken(code));
        } catch (WxErrorException e) {
            log.error("error", e);
            return ResponseUtil.failure(e.toString());
        }
    }

    /**
     * 根据code获取用户信息
     *
     * @param code
     * @param lang
     * @return
     */
    @RequestMapping("/user_info")
    public Object getUserInfo(@RequestParam(value = "code") String code, String lang) {
        try {
            return ResponseUtil.success(wxMpService.getOAuth2Service().getUserInfo(wxMpService.getOAuth2Service().getAccessToken(code), lang));
        } catch (WxErrorException e) {
            log.error("error", e);
            return ResponseUtil.failure(e.toString());
        }
    }

    /**
     * 刷新access_token
     *
     * @param refreshToken
     * @return
     */
    @RequestMapping("/refresh_token")
    public Object refreshToken(@RequestParam(value = "refreshToken") String refreshToken) {
        try {
            return wxMpService.getOAuth2Service().refreshAccessToken(refreshToken);
        } catch (WxErrorException e) {
            log.error("error", e);
            return ResponseUtil.failure(e.toString());
        }
    }
}
