package com.hlcy.yun.sys.redis;

import com.hlcy.yun.common.spring.redis.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.List;

// 启动时在测试类目录及其子目录下寻找启动类，若启动类不在上述目录，需显示指定，如：@SpringBootTest(classes = YunRightsApplication.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    DefaultRedisScript<Long> redisScript;

    @Autowired
    RedisUtils redisUtils;

    @Test
    void testLua() {
        redisUtils.delKeysWithSomePrefixByLua("rule::*");
    }

    @Test
    void testScan() {
        List<String> keys = redisUtils.scan("rule*");
        redisUtils.del(keys.toArray(new String[0]));
    }
}
