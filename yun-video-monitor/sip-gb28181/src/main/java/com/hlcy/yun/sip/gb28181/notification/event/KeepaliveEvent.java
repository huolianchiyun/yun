package com.hlcy.yun.sip.gb28181.notification.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class KeepaliveEvent implements DeviceEvent {
    private String type = "keepalive";
    private String deviceId;

    public KeepaliveEvent(String deviceId) {
        this.deviceId = deviceId;
    }

    public Map<String, String> toMap() {
        final Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("deviceId", deviceId);
        return map;
    }

    public static KeepaliveEvent valueOf(Map<String, String> map) {
        return new KeepaliveEvent(map.get("deviceId"));
    }
}
