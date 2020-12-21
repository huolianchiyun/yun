package com.hlcy.yun.sip.gb28181.notification;

import com.hlcy.yun.common.spring.SpringContextHolder;

public final class PublisherFactory {

    public static DeviceEventPublisher getDeviceEventPublisher(){
        return SpringContextHolder.getBean(DeviceEventPublisher.class);
    }
}
