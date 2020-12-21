package com.hlcy.yun.admincenter.modules.device.listener.redis;

import com.hlcy.yun.admincenter.modules.device.listener.DeviceEventHandler;
import com.hlcy.yun.sip.gb28181.notification.event.KeepaliveEvent;
import com.hlcy.yun.sip.gb28181.notification.event.LogoutEvent;
import com.hlcy.yun.sip.gb28181.notification.event.RegisterEvent;
import com.hlcy.yun.sip.stream.redis.RedisStreamProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisStreamListener implements StreamListener<String, MapRecord<String, String, String>> {

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisStreamProperties properties;
    private final DeviceEventHandler handler;

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        log.info("Receive a redis stream message, messageId={}, stream={}, body={}", message.getId(), message.getStream(), message.getValue());
        // 监听消息处理逻辑
        final Map<String, String> value = message.getValue();
        final String type = value.get("type");

        switch (type) {
            case "register":
                handler.handleRegisterEvent(RegisterEvent.valueOf(value));
                break;
            case "logout":
                handler.handleLogoutEvent(LogoutEvent.valueOf(value));
                break;
            case "keepalive":
                handler.handleKeepaliveEvent(KeepaliveEvent.valueOf(value));
                break;
            default:
        }
        // 通过RedisTemplate手动确认消息消费
        this.stringRedisTemplate.opsForStream().acknowledge(properties.getConsumerGroup(), message);
    }
}
