package com.zoe.weiya.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by chenghui on 2017/1/16.
 */
public class LuckyUserMessage implements Serializable {
    private String openId;
    private String name;
    private Integer degree;
    private Timestamp signDate;
    private String depName;
    private Timestamp hitTime;
}
