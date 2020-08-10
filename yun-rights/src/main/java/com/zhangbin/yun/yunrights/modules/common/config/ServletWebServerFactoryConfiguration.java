package com.zhangbin.yun.yunrights.modules.common.config;

import org.springframework.beans.BeansException;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class ServletWebServerFactoryConfiguration implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        TomcatServletWebServerFactory webServerFactory = applicationContext.getBean(TomcatServletWebServerFactory.class);
        if (Objects.nonNull(webServerFactory)) {
            webServerFactory.addConnectorCustomizers(connector -> connector.setProperty("relaxedQueryChars", "[]{}"));
        }
    }
}
