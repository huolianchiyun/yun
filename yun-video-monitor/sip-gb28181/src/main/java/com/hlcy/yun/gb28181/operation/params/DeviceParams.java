package com.hlcy.yun.gb28181.operation.params;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DeviceParams implements Serializable {
    private static final long serialVersionUID = 1L;

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
