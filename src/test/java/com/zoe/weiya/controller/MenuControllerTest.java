package com.zoe.weiya.controller;

import com.zoe.weiya.AbstractTestCase;
import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.util.JacksonJsonUtil;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.mp.api.WxMpService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by chenghui on 2017/1/4.
 */
public class MenuControllerTest extends AbstractTestCase {
//public class MenuControllerTest{
    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(MenuControllerTest.class);
    @Autowired WxMpService wxMpService;

    @Test
    public void testCreateMenu() throws Exception {
        WxMenu menu = new WxMenu();
        WxMenuButton button1 = new WxMenuButton();
        button1.setType(WxConsts.BUTTON_VIEW);
        button1.setName("新刊速递");
        button1.setUrl("http://mp.weixin.qq.com/mp/homepage?__biz=MzA5MjY0NzcwMw==&hid=22&sn=b6c0c5f5ae4fedf2bc0c7af4f2b50dcd#wechat_redirect");

        WxMenuButton button2 = new WxMenuButton();
        button2.setType(WxConsts.BUTTON_CLICK);
        button2.setName("内容精选");

        WxMenuButton button3 = new WxMenuButton();
        button3.setName("尾牙年会");

        menu.getButtons().add(button1);
        menu.getButtons().add(button2);
        menu.getButtons().add(button3);

        WxMenuButton button21 = new WxMenuButton();
        button21.setType(WxConsts.BUTTON_VIEW);
        button21.setName("本月书单");
        button21.setUrl("http://mp.weixin.qq.com/mp/homepage?__biz=MzA5MjY0NzcwMw==&hid=18&sn=11c0ea623c582e0963a4ce9f6164c1a8#wechat_redirect");

        WxMenuButton button22 = new WxMenuButton();
        button22.setType(WxConsts.BUTTON_VIEW);
        button22.setName("优秀智业人");
        button22.setUrl("http://mp.weixin.qq.com/mp/homepage?__biz=MzA5MjY0NzcwMw==&hid=6&sn=0c413aa54b962a4114695944569339a6#wechat_redirect");

        WxMenuButton button23 = new WxMenuButton();
        button23.setType(WxConsts.BUTTON_VIEW);
        button23.setName("精选合集");
        button23.setUrl("http://mp.weixin.qq.com/mp/homepage?__biz=MzA5MjY0NzcwMw==&hid=4&sn=249ef809e6b218ca621ff7ce324c1610#wechat_redirect");

        button2.getSubButtons().add(button21);
        button2.getSubButtons().add(button22);
        button2.getSubButtons().add(button23);

        WxMenuButton button31 = new WxMenuButton();
        button31.setType(WxConsts.BUTTON_CLICK);
        button31.setName("节目单");
        button31.setKey("card");

        WxMenuButton button32 = new WxMenuButton();
        button32.setType(WxConsts.BUTTON_CLICK);
        button32.setName("签到");
        button32.setKey("sign");

        WxMenuButton button33 = new WxMenuButton();
        button33.setType(WxConsts.BUTTON_CLICK);
        button33.setName("评论上墙");
        button33.setKey("comment");

        WxMenuButton button34 = new WxMenuButton();
        button34.setType(WxConsts.BUTTON_CLICK);
        button34.setName("投票");
        button34.setKey("vote");

        button3.getSubButtons().add(button31);
        button3.getSubButtons().add(button32);
        button3.getSubButtons().add(button33);
        button3.getSubButtons().add(button34);
        System.out.println(JacksonJsonUtil.beanToJson(menu));
        wxMpService.getMenuService().menuCreate(menu);

    }
}