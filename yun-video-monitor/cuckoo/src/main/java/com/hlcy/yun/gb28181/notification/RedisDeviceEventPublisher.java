package com.hlcy.yun.gb28181.notification;

import com.hlcy.yun.gb28181.notification.event.EventMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
class RedisDeviceEventPublisher implements DeviceEventPublisher {
    private final StringRedisTemplate delegate;
    private final RedisStreamProperties properties;

    @Override
    public void publishEvent(EventMap event) {
        this.delegate.opsForStream().add(StreamRecords.mapBacked(event.toMap()).withStreamKey(properties.getKey()));
    }
}
