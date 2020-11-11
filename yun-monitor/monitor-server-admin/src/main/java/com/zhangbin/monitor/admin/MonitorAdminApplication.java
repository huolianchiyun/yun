package com.zhangbin.monitor.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
public class MonitorAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(MonitorAdminApplication.class, args);
    }
}
