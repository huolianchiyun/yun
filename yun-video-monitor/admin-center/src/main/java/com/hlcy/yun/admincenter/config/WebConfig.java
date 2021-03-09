package com.hlcy.yun.admincenter.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.apache.tomcat.websocket.server.WsSci;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Value("${server.port}")
    private int sslPort;

    @Value("${server.http-port}")
    private int httpPort;

    @Value("${server.http-to-https}")
    private boolean httpToHttps;

    @Bean
    public ServletWebServerFactory servletContainerFactory() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                if (httpToHttps) {
                    //设置安全性约束
                    SecurityConstraint securityConstraint = new SecurityConstraint();
                    securityConstraint.setUserConstraint("CONFIDENTIAL");
                    //设置约束条件
                    SecurityCollection collection = new SecurityCollection();
                    //拦截所有请求
                    collection.addPattern("/*");
                    securityConstraint.addCollection(collection);
                    context.addConstraint(securityConstraint);
                } else {
                    super.postProcessContext(context);
                }
            }
        };
        tomcat.addAdditionalTomcatConnectors(buildHttpConnector());
        return tomcat;
    }

    private Connector buildHttpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(httpPort);
        if (httpToHttps) {
            // true： http使用http, https使用https;
            // false： http重定向到https;
            connector.setSecure(false);
            // 重定向端口号(非SSL到SSL)
            connector.setRedirectPort(sslPort);
        }
        return connector;
    }

    /**
     * 创建wss协议接口
     */
    @Bean
    public TomcatContextCustomizer tomcatContextCustomizer() {
        return context -> context.addServletContainerInitializer(new WsSci(), null);
    }
}
