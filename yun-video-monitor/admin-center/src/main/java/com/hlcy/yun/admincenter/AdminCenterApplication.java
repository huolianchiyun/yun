package com.hlcy.yun.admincenter;

import com.hlcy.yun.common.web.websocket.EnableWebSocket;
import com.hlcy.yun.sys.auto.EnableYunRights;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableYunRights
@SpringBootApplication
@EnableWebSocket
public class AdminCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminCenterApplication.class, args);
    }

}
