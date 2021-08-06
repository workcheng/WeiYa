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
    private String username;
    private String password;
    private String closeTime;
    private String openTime;
    private String sinOpenTime;
    private String signCloseTime;
    private String signInUrl;
    private String signOutUrl;
    private String voteUrl;
    private String commentUrl;
    private String cardUrl;
    private String prizeMessage;
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 2021-08-10 23:15:00
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
