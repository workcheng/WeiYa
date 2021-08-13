package com.workcheng.weiya.repository;

import com.workcheng.weiya.common.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author chenghui
 * @date 2021/8/12 16:45
 */
@SpringBootTest
class UserRepositoryTest {
    @Autowired private UserRepository userRepository;

    @Test
    void takeUserByRandom() {
        for (int i = 0; i<20; i++) {
            final User user = new User();
            user.setOpenId(i+"_user");
            user.setName("hello_"+ i);
            userRepository.save(user);
        }
        final List<User> users = userRepository.takeUserByRandom(10);
        System.out.println(users);
    }
}