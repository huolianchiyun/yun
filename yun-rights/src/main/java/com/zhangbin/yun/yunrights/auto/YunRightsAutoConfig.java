package com.zhangbin.yun.yunrights.auto;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.*;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import java.util.Objects;

@EnableConfigurationProperties(MybatisProperties.class)
@ComponentScan(value = YunRightsAutoConfig.SCAN_PACKAGE)
@MapperScan(value = YunRightsAutoConfig.SCAN_PACKAGE, annotationClass = Mapper.class)
public class YunRightsAutoConfig implements ApplicationContextAware {
    static final String SCAN_PACKAGE ="com.zhangbin.yun.yunrights.modules";

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        try {
            TomcatServletWebServerFactory webServerFactory = applicationContext.getBean(TomcatServletWebServerFactory.class);
            if (Objects.nonNull(webServerFactory)) {
                webServerFactory.addConnectorCustomizers(connector -> connector.setProperty("relaxedQueryChars", "[]{}"));
            }
        } catch (Exception e) {
        }
    }

}
