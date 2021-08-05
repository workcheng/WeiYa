package com.workcheng.weiya.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by chenghui on 2016/12/23.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyDate {
    private Integer year;
    private Integer month;
    private Integer day;
    private Integer hour;
    private Integer minute;
    private Integer second;

    @Override
    public String toString() {
        return "ZoeDate{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", hour=" + hour +
                ", minute=" + minute +
                ", second=" + second +
                '}';
    }
}
