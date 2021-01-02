package com.hlcy.yun.gb28181.config;

import com.hlcy.yun.common.spring.redis.RedisUtils;
import com.hlcy.yun.gb28181.sip.SipLayer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({ GB28181Properties.class})
@ComponentScan("com.hlcy.yun.sip")
public class GB28181AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SipLayer sipLayer(GB28181Properties properties, RedisUtils redisUtils) throws Exception {
        return new SipLayer(properties);
    }
}
