package com.zhangbin.yun.common.autoconfigure;

import com.zhangbin.yun.common.spring.redis.RedisUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({RedisUtils.class})
public class CommonAutoConfigure {
}
