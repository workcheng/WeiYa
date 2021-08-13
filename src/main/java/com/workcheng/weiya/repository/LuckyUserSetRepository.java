package com.workcheng.weiya.repository;

import com.workcheng.weiya.common.domain.LuckyUserMessage;
import com.workcheng.weiya.common.domain.LuckyUserSet;
import org.springframework.data.repository.CrudRepository;

/**
 * @author chenghui
 * @date 2021/8/12 16:00
 */
public interface LuckyUserSetRepository extends CrudRepository<LuckyUserSet, Long> {

    /**
     * 通过openId查找
     * @param openId
     * @return
     */
    LuckyUserSet findByOpenId(String openId);
}
