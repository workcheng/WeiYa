package com.workcheng.weiya.repository;

import com.workcheng.weiya.common.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author chenghui
 * @date 2021/8/5 16:56
 */
public interface UserRepository extends JpaRepository<User, Long> {
}