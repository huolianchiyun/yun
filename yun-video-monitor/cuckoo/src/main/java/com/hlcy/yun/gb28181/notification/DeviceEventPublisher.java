package com.hlcy.yun.gb28181.notification;


import com.hlcy.yun.gb28181.notification.event.EventMap;

public interface DeviceEventPublisher {

    /**
     * 发布设备事件
     *
     * @param event register event
     */
    void publishEvent(EventMap event);

}
