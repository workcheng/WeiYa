package com.zoe.weiya.model;

/**
 * Created by chenghui on 2016/12/23.
 */
public class ZoeDate {

    private String day;
    private String hour;
    private String minute;

    public ZoeDate(String day, String hour, String minute) {
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    @Override
    public String toString() {
        return "ZoeDate{" +
                "day=" + day +
                ", hour='" + hour + '\'' +
                ", minute='" + minute + '\'' +
                '}';
    }
}
