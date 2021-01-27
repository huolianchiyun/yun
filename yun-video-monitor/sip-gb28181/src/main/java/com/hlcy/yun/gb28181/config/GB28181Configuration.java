package com.hlcy.yun.gb28181.config;

import com.hlcy.yun.gb28181.service.RequestProcessorFactoryImpl;
import com.hlcy.yun.gb28181.service.sipmsg.RegisterProcessorImpl;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.sip.SipLayer;
import com.hlcy.yun.gb28181.sip.message.handler.RequestHandler;
import com.hlcy.yun.gb28181.sip.message.handler.request.RegisterRequestHandler;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({GB28181Properties.class})
@ComponentScan("com.hlcy.yun.gb28181")
public class GB28181Configuration implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        init(applicationContext.getBean(GB28181Properties.class));
    }

    private void init(GB28181Properties properties) {
        SipLayer.start(properties);
        FlowContextCacheUtil.init();
        RequestHandler.setProcessorFactory(new RequestProcessorFactoryImpl());
        RegisterRequestHandler.setRegisterProcessor(new RegisterProcessorImpl());
    }
}
