package com.hlcy.yun.admincenter.modules.device.listener.event;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class AlarmEvent extends AbstractDeviceEvent implements EventMap {
    private Device device;

    public AlarmEvent(Device device) {
        this.deviceId = device.getDeviceId();
        this.device = device;
        type = "alarm";
    }

    public Map<String, String> toMap() {
        final Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("deviceId", deviceId);
        map.put("device", JSON.toJSONString(device));
        return map;
    }

    public static AlarmEvent valueOf(Map<String, String> map) {
        return new AlarmEvent(JSON.parseObject(map.get("device"), Device.class));
    }
}
