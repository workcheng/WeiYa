package com.workcheng.weiya.common.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 *
 * @author andy
 * @date 2017/1/5
 */
@Data
@Entity
public class WsMessage implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank
    private String content;
    private String headImgUrl;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    @Override
    public String toString() {
        return "ZoeMessage{" +
                "content='" + content + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                '}';
    }
}
