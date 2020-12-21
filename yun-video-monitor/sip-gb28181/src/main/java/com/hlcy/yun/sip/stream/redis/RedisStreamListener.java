package com.hlcy.yun.sip.stream.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisStreamListener implements StreamListener<String, MapRecord<String, String, String>> {

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisStreamProperties properties;

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        log.info("Receive a redis stream message, messageId={}, stream={}, body={}", message.getId(), message.getStream(), message.getValue());
        // TODO 监听消息处理逻辑



        // 通过RedisTemplate手动确认消息消费
        this.stringRedisTemplate.opsForStream().acknowledge(properties.getConsumerGroup(), message);
    }
}
