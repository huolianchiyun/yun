package com.hlcy.yun.gb28181.bean.api;

import lombok.Getter;
import lombok.Setter;

/**
 * 看守位控制 API 参数
 */
@Getter
@Setter
public class HomePositionParams extends DeviceParams {
    /**
     * 看守位使能 1:开启, 0:关闭(必选)
     */
    private int enabled;

    /**
     * 自动归位时间间隔,开启看守位时使用,单位:秒(s)(可选)
     */
    private int resetTime;

    /**
     * 调用预置位编号,开启看守位时使用,取值范围 0~255 (可选)
     */
    private int presetIndex;
}
