package com.workcheng.weiya.common.dto;

import com.workcheng.weiya.common.domain.User;
import com.workcheng.weiya.common.dto.MyDate;
import lombok.Data;

import java.util.List;

/**
 *
 * @author chenghui
 * @date 2017/1/9
 */
@Data
public class UserListCount {
    private int orderCount;
    private List<User> users;
    private MyDate now;

    @Override
    public String toString() {
        return "UserListCount{" +
                "orderCount=" + orderCount +
                ", users=" + users +
                ", now=" + now +
                '}';
    }
}
