package com.zoe.weiya.model.responseModel;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * Created by chenghui on 2017/1/5.
 */
public class ZoeMessage implements Serializable {
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
