package com.hlcy.yun.common.spring.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

@Configuration
public class CommonRedisConfig {
    @Bean
    public RedisScript<Long> redisScript() {// 返回值要是 Long，不支持 Integer
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/del_keys_with_same_prefix.lua")));
        redisScript.setResultType(Long.class);
        return redisScript;
    }
}
