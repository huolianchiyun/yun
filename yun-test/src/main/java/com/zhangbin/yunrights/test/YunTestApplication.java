package com.zhangbin.yunrights.test;

import com.zhangbin.yun.yunrights.auto.EnableYunRights;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableYunRights
@SpringBootApplication
public class YunTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(YunTestApplication.class, args);
    }

}
