package com.zoe.weiya.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @author Binary Wang
 *
 */
@Configuration
public class WxMpConfig {
  @Value("#{wxProperties.wx_token}")
  private String token;

  @Value("#{wxProperties.wx_appid}")
  private String appid;

  @Value("#{wxProperties.wx_appsecret}")
  private String appsecret;

  @Value("#{wxProperties.wx_aeskey}")
  private String aesKey;

  public String getToken() {
    return this.token;
  }

  public String getAppid() {
    return this.appid;
  }

  public String getAppsecret() {
    return this.appsecret;
  }

  public String getAesKey() {
    return this.aesKey;
  }

}
