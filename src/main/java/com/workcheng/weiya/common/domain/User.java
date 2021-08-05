package com.workcheng.weiya.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 *
 * @author andy
 * @date 2016/12/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_user")
public class User extends UnionUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "openId不能为空")
    @Column(unique = true)
    private String openId;
    @NotBlank(message = "姓名不能为空")
    private String name;
    //    @NotNull(message = "是否订餐不能为空")
    @Column(name="`ORDER`")
    private Integer order;
    private String nickName;
    private Integer priceCount;
    //该时间段内的签到标识符
    private String signFlag;
    private Timestamp signDate = new Timestamp(System.currentTimeMillis());

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", openId='" + openId + '\'' +
                ", name='" + name + '\'' +
                ", order=" + order +
                ", nickName='" + nickName + '\'' +
                ", priceCount=" + priceCount +
                ", signFlag='" + signFlag + '\'' +
                ", signDate=" + signDate +
                '}';
    }
}
