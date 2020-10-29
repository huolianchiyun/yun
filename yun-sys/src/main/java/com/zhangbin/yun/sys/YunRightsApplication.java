package com.zhangbin.yun.sys;

import com.zhangbin.yun.common.autoconfigure.EnableWebSocket;
import com.zhangbin.yun.sys.modules.common.annotation.rest.AnonymousGetMapping;
import io.swagger.annotations.Api;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

@EnableWebSocket
@RestController
@Api(hidden = true)
@SpringBootApplication
public class YunRightsApplication {

    public static void main(String[] args) {
        SpringApplication.run(YunRightsApplication.class, args);

    }

    @Bean
    public ServletWebServerFactory servletWebServerFactory(){
        return new TomcatServletWebServerFactory();
    }

    /**
     * 访问首页提示
     *
     * @return /
     */
    @AnonymousGetMapping("/")
    public String index() {
        return "Yun Backend service started successfully";
    }

}
