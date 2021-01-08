package com.hlcy.yun.admincenter.modules.device.listener;


import com.hlcy.yun.admincenter.modules.device.listener.event.KeepaliveEvent;
import com.hlcy.yun.admincenter.modules.device.listener.event.LogoutEvent;
import com.hlcy.yun.admincenter.modules.device.listener.event.RegisterEvent;

public interface DeviceEventHandler {

    /**
     * 处理设备注册事件
     *
     * @param event register event
     */
    void handleRegisterEvent(RegisterEvent event);

    /**
     * 处理设备注销事件
     *
     * @param event logout event
     */
    void handleLogoutEvent(LogoutEvent event);

    /**
     * 处理设备心跳事件
     *
     * @param event Keepalive event
     */
    void handleKeepaliveEvent(KeepaliveEvent event);
}
