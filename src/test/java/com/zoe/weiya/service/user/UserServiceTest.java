package com.zoe.weiya.service.user;

import com.zoe.weiya.AbstractTestCase;
import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.model.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by andy on 2016/12/21.
 */
public class UserServiceTest extends AbstractTestCase {
    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(UserServiceTest.class);
    @Autowired UserService userService;

    @Test
    public void saveInSet() throws Exception {
        User u = new User();
        u.setName("ch");
        u.setOpenid("001");
        log.info("info:"+userService.saveInSet(u));
    }

    @Test
    public void getUserSet() throws Exception {
        log.info("info:"+ userService.getUserSet());
    }

}