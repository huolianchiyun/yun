package com.zhangbin.yun.yunrights.modules.common.config;

import com.zhangbin.yun.yunrights.modules.common.utils.SpringContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringContextConfig {

    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

}
