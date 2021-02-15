package com.hlcy.yun.gb28181.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisStreamController {

    @Autowired
    RedisDeviceEventPublisher publisher;


    @GetMapping("/addStream")
    public void addValueStream() {

//        // 创建消息记录, 以及指定stream
//        StringRecord stringRecord = StreamRecords.string(Collections.singletonMap("name", "KevinBlandy")).withStreamKey(streamKey);
//        RecordId recordId = this.stringRedisTemplate.opsForStream().add(stringRecord);
//        publisher.publishEvent(new RegisterEvent(new Device("123456", null)));
    }

}
