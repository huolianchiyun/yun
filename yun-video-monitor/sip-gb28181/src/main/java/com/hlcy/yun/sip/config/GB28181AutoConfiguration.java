package com.hlcy.yun.sip.config;

import com.hlcy.yun.sip.SipLayer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({ GB28181Properties.class})
public class GB28181AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SipLayer sipLayer(GB28181Properties properties) throws Exception {
        return new SipLayer(properties);
    }
}
