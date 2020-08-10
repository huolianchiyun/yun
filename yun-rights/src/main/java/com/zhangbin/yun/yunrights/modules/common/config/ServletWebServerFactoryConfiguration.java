package com.zhangbin.yun.yunrights.modules.common.config;

import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.UpgradeProtocol;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Objects;

@Configuration
public class ServletWebServerFactoryConfiguration  implements ApplicationContextAware {


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        TomcatServletWebServerFactory webServerFactory = applicationContext.getBean(TomcatServletWebServerFactory.class);
        if(Objects.nonNull(webServerFactory)){
            webServerFactory.addConnectorCustomizers(connector -> connector.setProperty("relaxedQueryChars", "[]{}"));
        }
    }
}
