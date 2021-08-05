package com.workcheng.weiya.common.config;

import com.workcheng.weiya.common.filter.AuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenghui
 * @date 2021/8/5 10:24
 */
@Configuration
@RequiredArgsConstructor
public class FilterConfiguration {
    private final WeiYaConfig weiYaConfig;

    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AuthFilter());
        registration.addUrlPatterns("/admin/*", "/danmu/danmu.html", "/lottery/lottery.html", "/lottery/lottery.new.html");
        registration.setName("whitelistFilter");
        registration.setOrder(1);
        registration.addInitParameter("username", weiYaConfig.getUsername());
        registration.addInitParameter("password", weiYaConfig.getPassword());
        // 设置过滤器被调用的顺序
        return registration;
    }
}