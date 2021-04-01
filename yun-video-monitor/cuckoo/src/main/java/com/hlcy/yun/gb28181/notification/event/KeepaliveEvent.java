package com.hlcy.yun.gb28181.notification.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class KeepaliveEvent extends AbstractDeviceEvent implements EventMap {
    private String proxyIp;
    private int devicePort;

    public KeepaliveEvent(String deviceId) {
        this.deviceId = deviceId;
        this.type = "keepalive";
    }

    @Override
    public Map<String, String> toMap() {
        final Map<String, String> map = new HashMap<>(4);
        map.put("type", type);
        map.put("deviceId", deviceId);
        map.put("proxyIp", proxyIp);
        map.put("devicePort", String.valueOf(devicePort));
        return map;
    }

    public static KeepaliveEvent valueOf(Map<String, String> map) {
        return new KeepaliveEvent(map.get("deviceId"));
    }
}
