package com.zhangbin.yun.common.autoconfigure;

import com.zhangbin.yun.common.filter.CrossDomainFilter;
import com.zhangbin.yun.common.filter.PageHelperFilter;
import com.zhangbin.yun.common.spring.redis.RedisUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({RedisUtils.class, CrossDomainFilter.class, PageHelperFilter.class})
public class CommonAutoConfigure {
}
