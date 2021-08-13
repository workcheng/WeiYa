package com.workcheng.weiya.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.workcheng.weiya.common.dto.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author andy
 * @date 2017/1/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Message extends LuckyUser implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String message;
    private Date createTime;
}
