package com.hlcy.yun.gb28181.bean.api;

/**
 * 拉框放大/缩小控制 API 参数
 */
public class DragZoomParams extends DeviceParams {
    /**
     * 播放窗口长度像素值
     */
    private int windowLength;
    /**
     * 播放窗口宽度像素值
     */
    private int windowWidth;

    /**
     * 拉框中心的横轴坐标像素值
     */
    private int midPointX;
    /**
     * 拉框中心的纵轴坐标像素值
     */
    private int midPointY;
    /**
     * 拉框长度像素值
     */
    private int lengthX;
    /**
     * 拉框宽度像素值
     */
    private int lengthY;

}
