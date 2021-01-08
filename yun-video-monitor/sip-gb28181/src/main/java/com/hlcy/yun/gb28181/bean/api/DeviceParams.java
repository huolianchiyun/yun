package com.hlcy.yun.gb28181.bean.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceParams {
    /**
     * 设备通道 Id
     */
    protected String channelId;

    /**
     * 设备联网IP
     */
    protected String deviceIp;

    /**
     * 设备联网端口
     */
    protected int  devicePort;

    /**
     * 传输协议：UDP/TCP
     */
    protected String deviceTransport;
}
