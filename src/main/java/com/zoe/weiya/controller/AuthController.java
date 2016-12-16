package com.zoe.weiya.controller;

import com.zoe.weiya.comm.Respon.ZoeObject;
import com.zoe.weiya.comm.constant.CommonConstant;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by andy on 2016/12/16.
 */
@RequestMapping("auth")
@Controller
public class AuthController {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AuthController.class);
    @Autowired
    protected WxMpServiceImpl wxMpService;

    @RequestMapping(value="url",method = RequestMethod.GET)
    @ResponseBody
    public Object getAuthUrl(String url,String state){
        return ZoeObject.success(wxMpService.oauth2buildAuthorizationUrl(url, CommonConstant.AUTH_USERINFO,state));
    }

    @RequestMapping("access_token")
    @ResponseBody
    public Object getAccessToken(String code){
        try {
            return ZoeObject.success(wxMpService.oauth2getAccessToken(code));
        } catch (WxErrorException e) {
            log.error(CommonConstant.ERROR+e.toString());
            return ZoeObject.failure(e.toString());
        }
    }
}
