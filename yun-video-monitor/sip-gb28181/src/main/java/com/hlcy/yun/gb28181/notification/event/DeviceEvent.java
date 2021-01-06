package com.hlcy.yun.gb28181.notification.event;

import com.alibaba.fastjson.JSON;
import com.hlcy.yun.gb28181.bean.Device;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class DeviceEvent extends AbstractDeviceEvent implements EventMap {
    private Device device;

    public DeviceEvent(String type, Device device) {
        this.deviceId = device.getDeviceId();
        this.device = device;
        this.type = type;
    }


    public Map<String, String> toMap() {
        final Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("deviceId", deviceId);
        map.put("device", JSON.toJSONString(device));
        return map;
    }

    public static DeviceEvent valueOf(Map<String, String> map) {
        return new DeviceEvent(map.get("type"),JSON.parseObject(map.get("device"), Device.class));
    }
}
