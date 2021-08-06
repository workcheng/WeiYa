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
    private String signInUrl;
    private String signOutUrl;
    private String voteUrl;
    private String commentUrl;
    private String cardUrl;
    private String prizeMessage;
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 2021-08-10 23:15:00
    public boolean partyTime() {
        final Date now = new Date();
        log.info("now:{}", now);
        if (openTime == null) {
            return false;
        }
        if (closeTime == null) {
            return false;
        }
        final Date open;
        try {
            open = simpleDateFormat.parse(openTime);
        } catch (ParseException e) {
            log.error("format time error, openTime" + openTime, e);
            return false;
        }
        final Date close;
        try {
            close = simpleDateFormat.parse(closeTime);
        } catch (ParseException e) {
            log.error("format time error, closeTime:"+ closeTime, e);
            return false;
        }
        if (now.after(close)) {
            return false;
        }
        return !now.before(open);
    }
}
