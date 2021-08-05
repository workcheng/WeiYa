package com.workcheng.weiya.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenghui
 * @date 2021/8/4 15:33
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wx.mp.config-storage.redis")
public class WxRedisConfig {
    private String host;
    private String password;
    private Integer port;
}
