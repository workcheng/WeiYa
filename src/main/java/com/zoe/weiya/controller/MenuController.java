package com.zoe.weiya.controller;

import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.comm.properties.ZoeProperties;
import com.zoe.weiya.comm.response.ZoeObject;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by chenghui on 2016/12/27.
 */
@RequestMapping("menu")
@RestController
public class MenuController {
    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(AuthController.class);
    @Autowired
    protected WxMpServiceImpl wxMpService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Object getMenu(){
        try {
            return ZoeObject.success(wxMpService.getMenuService().menuGet());
        } catch (WxErrorException e) {
            log.error("error",e);
            return ZoeObject.failure();
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object createMenu(@RequestBody WxMenu wxMenu){
        try {
            wxMpService.getMenuService().menuCreate(wxMenu);
            return ZoeObject.success();
        } catch (WxErrorException e) {
            log.error("error",e);
            return ZoeObject.failure();
        }
    }

    @RequestMapping(value = "default", method = RequestMethod.POST)
    public Object createMenu(){
        try {
            WxMenu wxMenu = WxMenu.fromJson(ZoeProperties.getMenuJson());
            wxMpService.getMenuService().menuCreate(wxMenu);
            return ZoeObject.success();
        } catch (WxErrorException e) {
            log.error("error",e);
            return ZoeObject.failure();
        } catch (Exception e) {
            log.error("error",e);
            return ZoeObject.failure();
        }
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public Object deleteMenu(){
        try {
            wxMpService.getMenuService().menuDelete();
            return ZoeObject.success();
        } catch (WxErrorException e) {
            log.error("error",e);
            return ZoeObject.failure();
        }
    }
}
