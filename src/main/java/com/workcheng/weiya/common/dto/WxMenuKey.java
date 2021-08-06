package com.workcheng.weiya.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 菜单的dto对象
 *
 * @author andy
 */
@Data
@NoArgsConstructor
public class WxMenuKey {
    private String type;
    private String content;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
