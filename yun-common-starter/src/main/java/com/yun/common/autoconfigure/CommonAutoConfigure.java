package com.yun.common.autoconfigure;

import com.yun.common.spring.redis.RedisUtils;
import com.yun.common.filter.CrossDomainFilter;
import com.yun.common.filter.PageHelperFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({RedisUtils.class, CrossDomainFilter.class, PageHelperFilter.class})
public class CommonAutoConfigure {
}
