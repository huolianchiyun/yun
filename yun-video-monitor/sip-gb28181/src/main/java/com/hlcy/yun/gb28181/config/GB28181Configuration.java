package com.hlcy.yun.gb28181.config;

import com.hlcy.yun.common.spring.redis.RedisUtils;
import com.hlcy.yun.gb28181.operation.flow.FlowContextCache;
import com.hlcy.yun.gb28181.sip.SipLayer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({GB28181Properties.class})
@ComponentScan("com.hlcy.yun.gb28181")
public class GB28181Configuration implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        init(applicationContext.getBean(GB28181Properties.class), applicationContext.getBean(RedisUtils.class));
    }

    private void init(GB28181Properties properties, RedisUtils redisUtils) {
        SipLayer.start(properties);
        FlowContextCache.init(properties.getSipId(), redisUtils);
    }
}
