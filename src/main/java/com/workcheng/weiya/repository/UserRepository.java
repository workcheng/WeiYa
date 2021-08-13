package com.workcheng.weiya.repository;

import com.workcheng.weiya.common.domain.LuckyUserMessage;
import com.workcheng.weiya.common.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author chenghui
 * @date 2021/8/12 16:09
 */
public interface UserRepository extends CrudRepository<User, Long> {
    /**
     * 查找签到用户
     * @param openId
     * @return
     */
    User findByOpenId(String openId);

    /**
     * 随机获取用户
     * @param count
     * @return
     */
    @Query(nativeQuery = true, value = "select * from WY_USER order by rand() limit ?1")
    List<User> takeUserByRandom(Integer count);

    /**
     * 计算点餐人数
     * @param isOrder
     * @return
     */
    Long countAllByIsOrder(Integer isOrder);

    /**
     * 点餐的列表
     * @param isOrder
     * @return
     */
    List<User> getAllByIsOrder(Integer isOrder);
}
