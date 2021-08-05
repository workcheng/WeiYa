package com.workcheng.weiya;

import com.workcheng.weiya.common.config.WxRedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Nullable;

/**
 * @author binary wang
 */
@EnableScheduling
@RestController
@RequestMapping("/")
@SpringBootApplication
public class WeiYaApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeiYaApplication.class, args);
    }

    @Autowired
    private WxRedisConfig wxRedisConfig;

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

    public RedisStandaloneConfiguration redisStandaloneConfiguration0() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(wxRedisConfig.getHost());
        configuration.setPassword(RedisPassword.of(wxRedisConfig.getPassword()));
        configuration.setPort(wxRedisConfig.getPort());
        configuration.setDatabase(0);
        return configuration;
    }

    public RedisStandaloneConfiguration redisStandaloneConfiguration1() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(wxRedisConfig.getHost());
        configuration.setPassword(RedisPassword.of(wxRedisConfig.getPassword()));
        configuration.setPort(wxRedisConfig.getPort());
        configuration.setDatabase(1);
        return configuration;
    }

    public RedisStandaloneConfiguration redisStandaloneConfiguration2() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(wxRedisConfig.getHost());
        configuration.setPassword(RedisPassword.of(wxRedisConfig.getPassword()));
        configuration.setPort(wxRedisConfig.getPort());
        configuration.setDatabase(2);
        return configuration;
    }

    public RedisStandaloneConfiguration redisStandaloneConfiguration3() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(wxRedisConfig.getHost());
        configuration.setPassword(RedisPassword.of(wxRedisConfig.getPassword()));
        configuration.setPort(wxRedisConfig.getPort());
        configuration.setDatabase(3);
        return configuration;
    }

    public RedisStandaloneConfiguration redisStandaloneConfiguration4() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(wxRedisConfig.getHost());
        configuration.setPassword(RedisPassword.of(wxRedisConfig.getPassword()));
        configuration.setPort(wxRedisConfig.getPort());
        configuration.setDatabase(4);
        return configuration;
    }

    @Bean
    public JedisClientConfiguration clientConfiguration() {
        JedisClientConfiguration.JedisClientConfigurationBuilder builder = JedisClientConfiguration.builder();
        return builder.usePooling()
                .poolConfig(poolConfig())
                .build();
    }

    @Bean("redisTemplate0")
    public RedisTemplate redisTemplate0() {
        // 注意这里使用的是StringRedisTempalte
        StringRedisTemplate template = new StringRedisTemplate(redisConnectionFactory0());
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        // 设置值的序列化器
        template.setValueSerializer(jackson2JsonRedisSerializer);
        return template;
    }

    @Bean("redisTemplate1")
    public RedisTemplate redisTemplate1() {
        // 注意这里使用的是StringRedisTempalte
        StringRedisTemplate template = new StringRedisTemplate(redisConnectionFactory1());
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        // 设置值的序列化器
        template.setValueSerializer(jackson2JsonRedisSerializer);
        return template;
    }

    @Bean("redisTemplate2")
    public RedisTemplate redisTemplate2() {
        // 注意这里使用的是StringRedisTempalte
        StringRedisTemplate template = new StringRedisTemplate(redisConnectionFactory2());
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        // 设置值的序列化器
        template.setValueSerializer(jackson2JsonRedisSerializer);
        return template;
    }

    @Bean("redisTemplate3")
    public RedisTemplate redisTemplate3() {
        // 注意这里使用的是StringRedisTempalte
        StringRedisTemplate template = new StringRedisTemplate(redisConnectionFactory3());
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        // 设置值的序列化器
        template.setValueSerializer(jackson2JsonRedisSerializer);
        return template;
    }

    @Bean("redisTemplate4")
    public RedisTemplate redisTemplate4() {
        // 注意这里使用的是StringRedisTempalte
        StringRedisTemplate template = new StringRedisTemplate(redisConnectionFactory4());
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        // 设置值的序列化器
        template.setValueSerializer(jackson2JsonRedisSerializer);
        return template;
    }

    public JedisConnectionFactory redisConnectionFactory0() {
        return new JedisConnectionFactory(redisStandaloneConfiguration0(), clientConfiguration());
    }

    public JedisConnectionFactory redisConnectionFactory1() {
        return new JedisConnectionFactory(redisStandaloneConfiguration1(), clientConfiguration());
    }

    public JedisConnectionFactory redisConnectionFactory2() {
        return new JedisConnectionFactory(redisStandaloneConfiguration2(), clientConfiguration());
    }

    public JedisConnectionFactory redisConnectionFactory3() {
        return new JedisConnectionFactory(redisStandaloneConfiguration3(), clientConfiguration());
    }

    public JedisConnectionFactory redisConnectionFactory4() {
        return new JedisConnectionFactory(redisStandaloneConfiguration4(), clientConfiguration());
    }
}
