package com.workcheng.weiya.common.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chenghui
 * @date 2016/12/22
 */
@Data
public class UnionUser implements Serializable {
    private String openId;
    private String name;
    private String depName;
    private Integer order;
    private String headImgUrl;
    private String signFlag;
}
