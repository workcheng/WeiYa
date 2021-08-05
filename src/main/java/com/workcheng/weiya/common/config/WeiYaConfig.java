package com.workcheng.weiya.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Binary Wang
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "weiya")
public class WeiYaConfig {
    private String username;
    private String password;
    private String amClosingTime;
    private String amOpenIngTime;
    private String finalClosingTime;
    private String finalOpenIngTime;
    private String startDay;
    private String startMonth;
    private String startYear;
    private String noon;
    private String offWork;
    private String signInUrl;
    private String signOutUrl;
    private String voteUrl;
    private String commentUrl;
    private String cardUrl;
    private String prizeMessage;
}
