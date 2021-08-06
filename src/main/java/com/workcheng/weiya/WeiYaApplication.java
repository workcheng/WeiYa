package com.workcheng.weiya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Nullable;

/**
 * @author andy
 */
@EnableScheduling
@RestController
@RequestMapping("/")
@SpringBootApplication
public class WeiYaApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeiYaApplication.class, args);
    }

    @Bean
    @Nullable
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler threadPoolScheduler = new ThreadPoolTaskScheduler();
        threadPoolScheduler.setThreadNamePrefix("SockJS-");
        threadPoolScheduler.setPoolSize(Runtime.getRuntime().availableProcessors());
        threadPoolScheduler.setRemoveOnCancelPolicy(true);
        return threadPoolScheduler;
    }

    @Bean
    public JedisPoolConfig poolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(100);
        poolConfig.setMaxWaitMillis(30 * 1000);
        poolConfig.setMinIdle(20);
        poolConfig.setMaxIdle(40);
        poolConfig.setTestWhileIdle(true);
        return poolConfig;
    }
}
