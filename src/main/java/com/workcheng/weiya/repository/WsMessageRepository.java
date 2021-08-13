package com.workcheng.weiya.repository;

import com.workcheng.weiya.common.domain.WsMessage;
import org.springframework.data.repository.CrudRepository;

/**
 * @author chenghui
 * @date 2021/8/13 10:04
 */
public interface WsMessageRepository extends CrudRepository<WsMessage, Long> {
}
