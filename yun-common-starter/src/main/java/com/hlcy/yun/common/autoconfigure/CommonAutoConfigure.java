package com.hlcy.yun.common.autoconfigure;

import com.hlcy.yun.common.spring.redis.CommonRedisConfig;
import com.hlcy.yun.common.spring.redis.RedisUtils;
import com.hlcy.yun.common.filter.CrossDomainFilter;
import com.hlcy.yun.common.filter.PageHelperFilter;
import com.hlcy.yun.common.web.websocket.WebsocketEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({RedisUtils.class, CommonRedisConfig.class, CrossDomainFilter.class, PageHelperFilter.class, WebsocketEventPublisher.class})
public class CommonAutoConfigure {
}
