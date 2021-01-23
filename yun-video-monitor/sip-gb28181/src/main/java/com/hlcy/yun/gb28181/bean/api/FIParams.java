package com.hlcy.yun.gb28181.bean.api;

import lombok.Getter;
import lombok.Setter;

/**
 * 光圈控制和聚焦控制 API 参数
 */
@Getter
@Setter
public class FIParams extends DeviceParams {
    /**
     * 光圈
     * 0:停止 1:放大 2:缩小
     */
    private int iris;

    /**
     * 聚焦
     * 0:停止 1:远 2:近
     */
    private int focus;

    /**
     * 光圈速度
     * 速度范围由慢到快为 00H~FFH
     */
    private int irisSpeed;

    /**
     * 聚焦速度
     * 速度范围由慢到快为 00H~FFH
     */
    private int focusSpeed;

}
