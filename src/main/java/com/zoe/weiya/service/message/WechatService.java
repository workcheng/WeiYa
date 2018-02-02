package com.zoe.weiya.service.message;

import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.service.user.UserService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
  Created by andy on 2016/12/22.
 **/
@Service
public class WechatService {
    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(WechatService.class);
    @Autowired
    protected WxMpServiceImpl wxMpService;
    @Autowired
    protected UserService userService;


}
