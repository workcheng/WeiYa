package com.workcheng.weiya.common.dto;

import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author andy
 * @date 2016/12/29
 */
public class Vote {
    @NotBlank
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
