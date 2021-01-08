package com.hlcy.yun.gb28181.bean.api;

import lombok.Getter;
import lombok.Setter;

/**
 * 回放 API 参数
 */
@Getter
@Setter
public class PlaybackParams extends DeviceParams {
    private int playbackType;

    private long startTimestamp;

    private long endTimestamp;
}
