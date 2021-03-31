package com.hlcy.yun.gb28181.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class DeviceInfo {

    /**
     * 设备 Id: 目标设备/区域/系统的编码
     */
    private String deviceId;

    /**
     * 设备名
     */
    private String deviceName;

    /**
     * 设备联网IP
     */
    private String ip;

    private String proxyIp;

    /**
     * 设备联网端口
     */
    private int port;

    /**
     * 传输协议：UDP/TCP
     */
    private String transport;

    /**
     * 设备生产商
     */
    private String manufacturer;

    /**
     * 设备型号
     */
    private String model;

    /**
     * 设备固件版本
     */
    private String firmware;

}
