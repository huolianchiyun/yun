package com.hlcy.yun.gb28181.bean.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CruiseParams extends DeviceParams {
    private CruiseType type;

    /**
     * 巡航组号：范围 00H~FFH
     */
    private int groupNum;

    /**
     * 预置位号：范围 00H~FFH
     */
    private int presetIndex;

    private int speed;

    /**
     * 巡航停留时间的单位是秒(s)
     */
    private int duration;

    @Getter
    public enum CruiseType {
        ADD(0x84), DEL(0x85), SET_SPEED(0x86), SET_DURATION(0x87), START(0x88);

        private int code;

        CruiseType(int code) {
            this.code = code;
        }
    }
}
