package com.hlcy.yun.common.autoconfigure;

import com.hlcy.yun.common.spring.redis.RedisUtils;
import com.hlcy.yun.common.filter.CrossDomainFilter;
import com.hlcy.yun.common.filter.PageHelperFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({RedisUtils.class, CrossDomainFilter.class, PageHelperFilter.class})
public class CommonAutoConfigure {
}
