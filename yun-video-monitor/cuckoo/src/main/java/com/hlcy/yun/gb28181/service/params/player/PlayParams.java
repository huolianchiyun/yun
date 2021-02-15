package com.hlcy.yun.gb28181.service.params.player;

import com.hlcy.yun.gb28181.service.params.DeviceParams;
import lombok.Getter;
import lombok.Setter;

/**
 * 点播 API 参数
 */
@Getter
@Setter
public class PlayParams extends DeviceParams {
    /**
     * 视频码率
     */
    private String format;

    /**
     * 是否获取新流
     */
    private boolean newStream;
}
