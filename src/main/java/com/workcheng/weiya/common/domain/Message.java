package com.workcheng.weiya.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.workcheng.weiya.common.dto.*;

/**
 *
 * @author chenghui
 * @date 2017/1/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Message extends LuckyUser implements Serializable {
    private String message;
    private Date createTime;
}
