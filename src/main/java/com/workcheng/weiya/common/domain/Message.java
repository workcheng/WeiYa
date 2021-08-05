package com.workcheng.weiya.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
    private MyDate createTime;
}
