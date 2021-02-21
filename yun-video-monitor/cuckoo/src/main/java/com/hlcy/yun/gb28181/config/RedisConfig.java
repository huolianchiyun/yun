package com.hlcy.yun.gb28181.config;

import com.hlcy.yun.common.spring.redis.CommonRedisConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {
    @Bean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 创建模板类
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        // key序列化采用StringRedisSerializer
        template.setKeySerializer(new CommonRedisConfig.StringRedisSerializer());
        template.setHashKeySerializer(new CommonRedisConfig.StringRedisSerializer());
        return template;
    }
}
