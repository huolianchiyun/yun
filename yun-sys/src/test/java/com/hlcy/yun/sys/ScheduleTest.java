package com.hlcy.yun.sys;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class ScheduleTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleTest.class);

    @Scheduled(cron = "*/3 * * * * ?")
    public void testScheduler1() throws InterruptedException {
        LOGGER.info("--------------testScheduler1-------in--------");
        TimeUnit.SECONDS.sleep(60);
        LOGGER.info("--------------testScheduler1------out---------");
    }

    @Scheduled(cron = "*/3 * * * * ?")
    public void testScheduler2(){
        LOGGER.info("--------------testScheduler2---------------");
    }

    @Test
    void test() throws IOException {
        System.in.read();
    }

}

@Configuration
@EnableScheduling
class ScheduledConfigs {
    @Primary
    @Bean
    public TaskScheduler poolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(12);
        scheduler.setThreadNamePrefix("@schedule-");
        scheduler.initialize();
        return scheduler;
    }
}
