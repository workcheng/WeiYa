package com.workcheng.weiya.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Binary Wang
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wx.mp")
public class WxMpConfig {
    private String token;
    private String appId;
    private String secret;
    private String aesKey;

    public String getToken() {
        return this.token;
    }

    public String getAppId() {
        return this.appId;
    }

    public String getSecret() {
        return this.secret;
    }

    public String getAesKey() {
        return this.aesKey;
    }

}
