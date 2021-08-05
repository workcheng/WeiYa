package com.workcheng.weiya.common.dto;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author chenghui
 * @date 2021/8/5 15:58
 */
@Data
public class UserDto {
    private String name;
    private String signFlag;
    private String depName;
    private String openId;
    private String nickName;
    private Timestamp signDate;
    private String headImgUrl;
}
