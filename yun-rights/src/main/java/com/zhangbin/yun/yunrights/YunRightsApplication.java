package com.zhangbin.yun.yunrights;

import com.zhangbin.yun.yunrights.modules.common.annotation.rest.AnonymousGetMapping;
import com.zhangbin.yun.yunrights.modules.common.utils.SpringContextHolder;
import io.swagger.annotations.Api;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(hidden = true)
@SpringBootApplication
public class YunRightsApplication {

    public static void main(String[] args) {
        SpringApplication.run(YunRightsApplication.class, args);
    }
    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

    @Bean
    public ServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory fa = new TomcatServletWebServerFactory();
        fa.addConnectorCustomizers(connector -> connector.setProperty("relaxedQueryChars", "[]{}"));
        return fa;
    }

    /**
     * 访问首页提示
     *
     * @return /
     */
    @AnonymousGetMapping("/")
    public String index() {
        return "Backend service started successfully";
    }

}
