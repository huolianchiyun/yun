package com.hlcy.yun.gb28181.service.params.control;

import lombok.Getter;
import lombok.Setter;

/**
 * 设备配置控制 API 参数
 */
@Getter
@Setter
public class DeviceConfigControlParams extends ControlParams {

    public DeviceConfigControlParams() {
        super("DeviceConfig");
    }

    /**
     * 设备名称(可选)
     */
    private String deviceName;

    /**
     * 注册过期时间(可选)
     */
    private Integer expiration;

    /**
     * 心跳间隔时间(可选)
     */
    private Integer heartBeatInterval;

    /**
     * 心跳超时次数(可选)
     */
    private Integer heartBeatCount;

}
