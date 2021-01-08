package com.hlcy.yun.gb28181.bean.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PtzParams extends DeviceParams {
    private int leftRight;

    private int upDown;

    private int inOut;

    private int moveSpeed;

    private int zoomSpeed;
}
