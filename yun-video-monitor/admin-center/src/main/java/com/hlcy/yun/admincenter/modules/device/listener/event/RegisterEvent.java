package com.hlcy.yun.admincenter.modules.device.listener.event;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class RegisterEvent extends AbstractDeviceEvent implements EventMap {
    private Device device;

    public RegisterEvent(Device device) {
        this.device = device;
        this.deviceId = device.getDeviceId();
        this.type = "register";
    }

    public Map<String, String> toMap() {
        final Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("deviceId", deviceId);
        map.put("device", JSON.toJSONString(device));
        return map;
    }

    public static RegisterEvent valueOf(Map<String, String> map) {
        return new RegisterEvent(JSON.parseObject(map.get("device"), Device.class));
    }
}
