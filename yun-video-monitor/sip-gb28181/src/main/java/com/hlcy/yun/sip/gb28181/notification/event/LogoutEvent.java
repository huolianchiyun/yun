package com.hlcy.yun.sip.gb28181.notification.event;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class LogoutEvent implements DeviceEvent {
    private String type = "logout";
    private String deviceId;

    public LogoutEvent(String deviceId) {
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
