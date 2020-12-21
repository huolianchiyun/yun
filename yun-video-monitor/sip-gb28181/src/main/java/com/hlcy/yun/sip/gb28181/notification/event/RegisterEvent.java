package com.hlcy.yun.sip.gb28181.notification.event;

import com.alibaba.fastjson.JSON;
import com.hlcy.yun.sip.gb28181.bean.Device;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class RegisterEvent implements DeviceEvent {
    private String type = "register";
    private Device device;

    public RegisterEvent(Device device) {
        this.device = device;
    }

    public Map<String, String> toMap() {
        final Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("device", JSON.toJSONString(device));
        return map;
    }

    public static RegisterEvent valueOf(Map<String, String> map) {
        return new RegisterEvent(JSON.parseObject(map.get("device"), Device.class));
    }
}
