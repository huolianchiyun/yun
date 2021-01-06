package com.hlcy.yun.gb28181.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaybackInfo {
    private String deviceId;

    private String deviceIp;

    private int devicePort;

    private String channelId;

    private String transport;

    private int playbackType;

    private long startTimestamp;

    private long endTimestamp;
}
