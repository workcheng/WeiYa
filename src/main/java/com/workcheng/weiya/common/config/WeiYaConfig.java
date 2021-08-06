package com.workcheng.weiya.common.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Binary Wang
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "weiya")
@Slf4j
public class WeiYaConfig {
    /** 后台登陆用户名 */
    private String username;
    /** 后台登陆密码 */
    private String password;
    /** 活动结束时间 */
    private String closeTime;
    /** 活动开始时间 */
    private String openTime;
    /** 签到开始时间 */
    private String sinOpenTime;
    /** 签到结束时间 */
    private String signCloseTime;
    /** 签到 url */
    private String signInUrl;
    /** 签到 url */
    private String signOutUrl;
    /** 投票 url */
    private String voteUrl;
    /** 弹幕互动 url */
    private String commentUrl;
    /** 节目单 url */
    private String cardUrl;
    /** 中奖信息 */
    private String prizeMessage;
    /** 菜单书籍 */
    private String menuJson;
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public boolean weiyaTime() {
        return inTheTime(openTime, closeTime);
    }

    public boolean signTime() {
        return inTheTime(sinOpenTime, signCloseTime);
    }

    private boolean inTheTime(String start, String end) {
        final Date now = new Date();
        log.info("now:{}", now);
        if (start == null) {
            return false;
        }
        if (end == null) {
            return false;
        }
        final Date open;
        try {
            open = simpleDateFormat.parse(start);
        } catch (ParseException e) {
            log.error("format time error, start" + start, e);
            return false;
        }
        final Date close;
        try {
            close = simpleDateFormat.parse(end);
        } catch (ParseException e) {
            log.error("format time error, closeTime:"+ end, e);
            return false;
        }
        if (now.after(close)) {
            return false;
        }
        return !now.before(open);
    }

    public String getWeiyaTime() {
        return openTime + "-" + closeTime;
    }

    public String getSignTime() {
        return sinOpenTime + "-" + signCloseTime;
    }
}
