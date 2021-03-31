package com.hlcy.yun.gb28181.notification.event;

import com.alibaba.fastjson.JSON;
import com.hlcy.yun.gb28181.bean.DeviceInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class RegisterEvent extends AbstractDeviceEvent implements EventMap {
    private DeviceInfo device;

    public RegisterEvent(DeviceInfo device) {
        this.device = device;
        this.deviceId = device.getDeviceId();
        this.type = "register";
    }

    @Override
    public Map<String, String> toMap() {
        final Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("deviceId", deviceId);
        map.put("device", JSON.toJSONString(device));
        return map;
    }

    public static RegisterEvent valueOf(Map<String, String> map) {
        return new RegisterEvent(JSON.parseObject(map.get("device"), DeviceInfo.class));
    }
}
