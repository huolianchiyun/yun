package com.hlcy.yun.sys;

import com.hlcy.yun.common.web.websocket.EnableWebSocket;
import com.hlcy.yun.monitor.autoconfigure.EnableMonitor;
import com.hlcy.yun.sys.modules.common.annotation.rest.AnonymousGetMapping;
import io.swagger.annotations.Api;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@EnableMonitor
@EnableWebSocket
@RestController
@Api(hidden = true)
@SpringBootApplication
public class YunRightsApplication {

    public static void main(String[] args) {
        SpringApplication.run(YunRightsApplication.class, args);
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
