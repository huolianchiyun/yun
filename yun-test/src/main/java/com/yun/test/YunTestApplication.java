package com.yun.test;

import com.yun.sys.auto.EnableYunRights;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableMonitor
@EnableYunRights
@SpringBootApplication
public class YunTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(YunTestApplication.class, args);
    }
}
