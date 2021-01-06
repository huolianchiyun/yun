package com.hlcy.yun.gb28181.notification;

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
