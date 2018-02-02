package com.zoe.weiya.model.responseModel;

/**
 * Created by chenghui on 2017/1/12.
 */
public class BarrAgerModel {
    private int max;
    private int speed;
    private String img;
    private String info;
    private boolean close;
    private String color;
    private String href;
    private String old_ie_color;

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getOld_ie_color() {
        return old_ie_color;
    }

    public void setOld_ie_color(String old_ie_color) {
        this.old_ie_color = old_ie_color;
    }

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
