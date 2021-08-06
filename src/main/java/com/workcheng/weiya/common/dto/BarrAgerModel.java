package com.workcheng.weiya.common.dto;

import lombok.Data;

/**
 *
 * @author andy
 * @date 2017/1/12
 */
@Data
public class BarrAgerModel {
    private int max;
    private int speed;
    private String img;
    private String info;
    private boolean close;
    private String color;
    private String href;
    private String old_ie_color;

    @Override
    public String toString() {
        return "BarrAgerModel{" +
                "max=" + max +
                ", speed=" + speed +
                ", img='" + img + '\'' +
                ", info='" + info + '\'' +
                ", close=" + close +
                ", color='" + color + '\'' +
                ", href='" + href + '\'' +
                ", old_ie_color='" + old_ie_color + '\'' +
                '}';
    }
}
