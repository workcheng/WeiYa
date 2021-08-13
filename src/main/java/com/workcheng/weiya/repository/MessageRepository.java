package com.workcheng.weiya.repository;

import com.workcheng.weiya.common.domain.LuckyUserMessage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author chenghui
 * @date 2021/8/12 15:48
 */
public interface MessageRepository extends CrudRepository<LuckyUserMessage, Long> {

    /**
     * 根据中奖等级获取
     * @param degree
     * @return
     */
    List<LuckyUserMessage> getAllByDegree(Integer degree);
}
