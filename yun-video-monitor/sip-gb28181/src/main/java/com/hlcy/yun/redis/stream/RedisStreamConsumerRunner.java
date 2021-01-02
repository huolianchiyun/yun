package com.hlcy.yun.redis.stream;

import java.time.Duration;
import java.util.Collections;

import io.lettuce.core.RedisBusyException;
import io.lettuce.core.RedisCommandExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer.StreamMessageListenerContainerOptions;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisStreamConsumerRunner implements ApplicationRunner, DisposableBean {

    private StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer;

    private final RedisConnectionFactory redisConnectionFactory;
    // TODO 后续根据项目场景设置具体参数
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private final RedisStreamListener redisStreamListener;

    private final StringRedisTemplate stringRedisTemplate;

    private final RedisStreamProperties properties;

    @Override
    public void run(ApplicationArguments args) {
        createConsumerGroup(properties.getKey(), properties.getConsumerGroup());
        // 创建配置对象
        StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> containerOptions = StreamMessageListenerContainerOptions
                .builder()
                // 一次性最多拉取多少条消息
                .batchSize(10)
                // 执行消息轮询的执行器
                .executor(this.threadPoolTaskExecutor)
                // 消息消费异常的handler
                .errorHandler(Throwable::printStackTrace)
                // 超时时间，设置为0，表示不超时（超时后会抛出异常）
                .pollTimeout(Duration.ZERO)
                // 序列化器
                .serializer(new StringRedisSerializer())
                .build();

        // 根据配置对象创建监听容器对象
        StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer = StreamMessageListenerContainer
                .create(this.redisConnectionFactory, containerOptions);

        // 使用监听容器对象开始监听消费（使用的是手动确认方式）
        streamMessageListenerContainer.receive(Consumer.from(properties.getConsumerGroup(), properties.getConsumerName()),
                StreamOffset.create(properties.getKey(), ReadOffset.lastConsumed()), this.redisStreamListener);

        this.streamMessageListenerContainer = streamMessageListenerContainer;
        // 启动监听
        this.streamMessageListenerContainer.start();

    }

    @Override
    public void destroy() {
        this.streamMessageListenerContainer.stop();
    }

    private void createConsumerGroup(String key, String group) {
        try {
            stringRedisTemplate.opsForStream().createGroup(key, group);
        } catch (RedisSystemException e) {
            final Class<? extends Throwable> eClass = e.getRootCause().getClass();
            if (RedisBusyException.class.equals(eClass)) {
                log.warn("STREAM - Redis group already exists, skipping Redis group creation: {}", group);
            } else if (eClass.equals(RedisCommandExecutionException.class)) {
                log.warn("STREAM - Stream does not yet exist, creating empty stream: {}", key);
                stringRedisTemplate.opsForStream().add(key, Collections.singletonMap("", ""));
                stringRedisTemplate.opsForStream().createGroup(key, group);
            } else throw e;
        }
    }
}
