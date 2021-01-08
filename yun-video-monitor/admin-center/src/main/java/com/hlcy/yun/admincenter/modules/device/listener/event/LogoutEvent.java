package com.hlcy.yun.admincenter.modules.device.listener.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class LogoutEvent  extends AbstractDeviceEvent implements EventMap {

    public LogoutEvent(String deviceId) {
        this.type = "logout";
        this.deviceId = deviceId;
    }

    public Map<String, String> toMap() {
        final Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("deviceId", deviceId);
        return map;
    }

    public static LogoutEvent valueOf(Map<String, String> map) {
        return new LogoutEvent(map.get("deviceId"));
    }
}
