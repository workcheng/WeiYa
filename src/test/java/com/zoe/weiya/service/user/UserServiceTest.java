package com.zoe.weiya.service.user;

import com.zoe.weiya.AbstractTestCase;
import com.zoe.weiya.model.User;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserServiceTest extends AbstractTestCase{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceTest.class);
    @Autowired
    WxMpService wxMpService;
    @Autowired
    UserService userService;

    @org.junit.Test
    public void save() throws Exception {
        WxMpUserList wxMpUserList = wxMpService.getUserService().userList(null);
        if(null != wxMpUserList){
            List<String> openIds = wxMpUserList.getOpenids();
            if(null != openIds){
                for(int i=0; i<openIds.size(); i++){
                    WxMpUser wxMpUser = wxMpService.getUserService().userInfo(openIds.get(i));
                    if(null != wxMpUser){
                        User u = new User();
                        u.setName(wxMpUser.getNickname());
                        u.setDepName("区域产品中心");
                        u.setOpenId(wxMpUser.getOpenId());
                        u.setHeadImgUrl(wxMpUser.getHeadImgUrl());
                        userService.save(u);
                        LOGGER.info(wxMpUser.toString());
                    }
                }
            }
        }
    }


}
