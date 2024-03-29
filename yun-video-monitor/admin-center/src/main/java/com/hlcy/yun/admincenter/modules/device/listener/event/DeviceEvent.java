package com.hlcy.yun.admincenter.modules.device.listener.event;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
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


    @Override
    public Map<String, String> toMap() {
        final Map<String, String> map = new HashMap<>(3);
        map.put("type", type);
        map.put("deviceId", deviceId);
        map.put("device", JSON.toJSONString(device));
        return map;
    }

    public static DeviceEvent valueOf(Map<String, String> map) {
        return new DeviceEvent(map.get("type"),JSON.parseObject(map.get("device"), Device.class));
    }
}
