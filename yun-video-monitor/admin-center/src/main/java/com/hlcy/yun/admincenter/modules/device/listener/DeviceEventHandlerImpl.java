package com.hlcy.yun.admincenter.modules.device.listener;

import com.hlcy.yun.admincenter.modules.device.listener.event.KeepaliveEvent;
import com.hlcy.yun.admincenter.modules.device.listener.event.LogoutEvent;
import com.hlcy.yun.admincenter.modules.device.listener.event.RegisterEvent;
import org.springframework.stereotype.Component;

@Component
public class DeviceEventHandlerImpl implements DeviceEventHandler {
    @Override
    public void handleRegisterEvent(RegisterEvent event) {

    }

    @Override
    public void handleLogoutEvent(LogoutEvent event) {

    }

    @Override
    public void handleKeepaliveEvent(KeepaliveEvent event) {

    }
}
