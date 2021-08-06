package com.workcheng.weiya.common.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 *
 * @author andy
 * @date 2017/1/5
 */
@Data
public class LuckyUser {
    @NotBlank
    private String openId;
    @NotBlank
    private String name;
    @NotNull
    private Integer degree;

    @Override
    public String toString() {
        return "LuckyUser{" +
                "openId='" + openId + '\'' +
                ", degree=" + degree +
                '}';
    }
}
