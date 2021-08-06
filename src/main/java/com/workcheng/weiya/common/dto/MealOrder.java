package com.workcheng.weiya.common.dto;


import com.workcheng.weiya.common.domain.User;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by chenghui on 2017/1/3.
 */
@Data
public class MealOrder {
    private int orderCount;
    private List<User> orderUsers;
    private Date now;

    @Override
    public String toString() {
        return "MealOrder{" +
                "orderCount=" + orderCount +
                ", orderUsers=" + orderUsers +
                '}';
    }
}
