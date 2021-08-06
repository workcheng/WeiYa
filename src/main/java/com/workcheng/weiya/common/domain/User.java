package com.workcheng.weiya.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import java.sql.Timestamp;

/**
 *
 * @author andy
 * @date 2016/12/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class User extends UnionUser {
    @NotBlank(message = "openId不能为空")
    private String openId;
    @NotBlank(message = "姓名不能为空")
    private String name;
    /** 是否订餐不能为空 */
    private Integer order;
    private String nickName;
    private Integer priceCount;
    /** 该时间段内的签到标识符 */
    private String signFlag;
    private Timestamp signDate = new Timestamp(System.currentTimeMillis());

    @Override
    public String toString() {
        return "User{" +
                "openId='" + openId + '\'' +
                ", name='" + name + '\'' +
                ", order=" + order +
                ", nickName='" + nickName + '\'' +
                ", priceCount=" + priceCount +
                ", signFlag='" + signFlag + '\'' +
                ", signDate=" + signDate +
                '}';
    }
}
