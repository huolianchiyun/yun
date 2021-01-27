package com.hlcy.yun.gb28181.service.params;

import lombok.Getter;
import lombok.Setter;

/**
 * 云台控制参数，详情见：GB/T 28181-2016
 * A.3.2 PTZ指令
 */
@Getter
@Setter
public class PtzParams extends DeviceParams { //TODO 对参数值范围进行校验
    /**
     * 云台水平方向控制 0:停止 1:左移 2:右移
     */
    private int pan;

    /**
     * 水平控制速度相对值，速度范围由慢到快为00H~FFH
     */
    private int panSpeed;

    /**
     * 云台垂直方向控制 0:停止 1:上移 2:下移，速度范围由慢到快为00H-FFH
     */
    private int tilt;

    /**
     * 垂直控制速度相对值
     */
    private int tiltSpeed;

    /**
     *  镜头变倍（缩小放大） 0:停止 1:缩小 2:放大
     */
    private int zoom;

    /**
     * 变倍控制速度相对值，速度范围由慢到快为00H-FFH
     */
    private int zoomSpeed;
}
