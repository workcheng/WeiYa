package com.workcheng.weiya.common.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 中奖用户集合
 * @author chenghui
 * @date 2021/8/12 15:59
 */
@Data
@Entity
public class LuckyUserSet implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String openId;
    private Integer degree;
}
