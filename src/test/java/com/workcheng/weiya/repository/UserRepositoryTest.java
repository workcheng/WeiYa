package com.workcheng.weiya.repository;


import com.workcheng.weiya.common.domain.User;
import com.workcheng.weiya.common.utils.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

/**
 * @author chenghui
 * @date 2021/8/5 17:00
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveTest() throws Exception {
        User user = new User();
        user.setName("郑龙飞");
        user.setOpenId(RandomUtil.getRandNumber(10));
        user.setHeadImgUrl("http://merryyou.cn");
        User result = userRepository.save(user);
        log.info(result.toString());
        Assert.assertNotNull(user.getId());
    }

    @Test
    public void findOneTest() throws Exception{
        Optional<User> user = userRepository.findById(1L);
        log.info(user.toString());
        Assert.assertNotNull(user);
        Assert.assertTrue(1l==user.get().getId());
    }
}