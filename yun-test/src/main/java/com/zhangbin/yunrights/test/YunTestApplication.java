package com.zhangbin.yunrights.test;

import com.zhangbin.yun.monitor.autoconfigure.EnableMonitor;
import com.zhangbin.yun.sys.auto.EnableYunRights;
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
