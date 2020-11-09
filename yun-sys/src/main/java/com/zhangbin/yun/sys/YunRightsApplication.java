package com.zhangbin.yun.sys;

import com.zhangbin.yun.sys.modules.common.annotation.rest.AnonymousGetMapping;
import io.swagger.annotations.Api;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

//@EnableMonitor
//@EnableWebSocket
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
