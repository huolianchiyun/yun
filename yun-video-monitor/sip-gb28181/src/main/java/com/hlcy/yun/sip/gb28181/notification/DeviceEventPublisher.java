package com.hlcy.yun.sip.gb28181.notification;


import com.hlcy.yun.sip.gb28181.notification.event.DeviceEvent;

public interface DeviceEventPublisher {

    /**
     * 发布设备事件
     *
     * @param event register event
     */
    void publishEvent(DeviceEvent event);

}
