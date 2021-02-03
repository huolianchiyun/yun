package com.hlcy.yun.gb28181.service.params;

import com.hlcy.yun.gb28181.sip.message.factory.Transport;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DeviceParams implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 设备 Id
     */
    @ApiModelProperty(required = true)
    private String deviceId;

    /**
     * 设备通道 Id，视频通道或语音通道
     */
    @ApiModelProperty(required = true)
    protected String channelId;

    /**
     * 设备联网IP
     */
    @ApiModelProperty(required = true)
    protected String deviceIp;

    /**
     * 设备联网端口
     */
    @ApiModelProperty(required = true)
    protected int devicePort;

    /**
     * 传输协议：UDP/TCP
     */
    @ApiModelProperty(required = true)
    protected Transport deviceTransport;

}
