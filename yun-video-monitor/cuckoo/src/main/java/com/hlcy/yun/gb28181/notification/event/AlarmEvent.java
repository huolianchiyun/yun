package com.hlcy.yun.gb28181.notification.event;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class AlarmEvent extends AbstractDeviceEvent implements EventMap {
    private String alarmPriority;
    private String alarmMethod;
    private String alarmTime;
    private String alarmDescription;
    private String longitude;
    private String latitude;

    public AlarmEvent(String deviceId) {
        type = "alarm";
        this.deviceId = deviceId;
    }

    @Override
    public Map<String, String> toMap() {
        final Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("deviceId", deviceId);
        map.put("alarmPriority", alarmPriority);
        map.put("alarmMethod", alarmMethod);
        map.put("alarmTime", alarmTime);
        map.put("alarmDescription", alarmDescription);
        map.put("longitude", longitude);
        map.put("latitude", latitude);
        return map;
    }
}
