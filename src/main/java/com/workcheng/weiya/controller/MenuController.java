package com.workcheng.weiya.controller;

import com.workcheng.weiya.common.config.WeiYaConfig;
import com.workcheng.weiya.common.utils.PropertiesUtil;
import com.workcheng.weiya.common.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by chenghui on 2016/12/27.
 */
@RequestMapping("/menu")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MenuController {
    protected final WxMpServiceImpl wxMpService;
    protected final WeiYaConfig weiYaConfig;

    /**
     * 获取自定义菜单
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Object getMenu() {
        try {
            return ResponseUtil.success(wxMpService.getMenuService().menuGet());
        } catch (WxErrorException e) {
            log.error("error", e);
            return ResponseUtil.failure(e);
        }
    }

    /**
     * 创建自定义菜单
     *
     * @param wxMenu
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Object createMenu(@RequestBody WxMenu wxMenu) {
        try {
            wxMpService.getMenuService().menuCreate(wxMenu);
            return ResponseUtil.success();
        } catch (WxErrorException e) {
            log.error("error", e);
            return ResponseUtil.failure();
        }
    }

    /**
     * 从配置文件中创建自定义菜单
     *
     * @return
     */
    @RequestMapping(value = "/default", method = RequestMethod.POST)
    public Object createMenu() {
        try {
            WxMenu wxMenu = WxMenu.fromJson(PropertiesUtil.getMenuJson(weiYaConfig));
            wxMpService.getMenuService().menuCreate(wxMenu);
            return ResponseUtil.success();
        } catch (WxErrorException e) {
            log.error("error", e);
            return ResponseUtil.failure();
        } catch (Exception e) {
            log.error("error", e);
            return ResponseUtil.failure();
        }
    }

    /**
     * 删除自定义菜单
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public Object deleteMenu() {
        try {
            wxMpService.getMenuService().menuDelete();
            return ResponseUtil.success();
        } catch (WxErrorException e) {
            log.error("error", e);
            return ResponseUtil.failure();
        }
    }
}
