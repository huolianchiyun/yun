package com.hlcy.yun.sip.stream.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@lombok.Data
@Component
@ConfigurationProperties(prefix = "spring.redis.stream")
public class RedisStreamProperties {
    private String key;
    private String consumerGroup;
    private String consumerName;
}
