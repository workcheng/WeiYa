package com.workcheng.weiya.common.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

/**
 * @author chenghui
 * @date 2021/8/6 9:52
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig {

    /**
     * 配置自定义redisTemplate
     * @return
     */
    @Bean("redisTemplate0")
    RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 注意这里使用的是StringRedisTempalte
        StringRedisTemplate template = new StringRedisTemplate(redisConnectionFactory);
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        // 设置值的序列化器
        template.setValueSerializer(jackson2JsonRedisSerializer);
        return template;
    }

}
