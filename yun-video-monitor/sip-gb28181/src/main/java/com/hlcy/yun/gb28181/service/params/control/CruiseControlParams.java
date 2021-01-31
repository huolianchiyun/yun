package com.hlcy.yun.gb28181.service.params.control;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CruiseControlParams extends ControlParams {
    private CruiseType type;

    /**
     * bit5
     * 巡航组号：范围 00H~FFH
     */
    private int groupNum;

    /**
     * bit6
     * 预置位号：范围 00H~FFH, 为00H时，表示删除对应的整条巡航
     */
    private int presetIndex;

    /**
     * bit7
     * 巡航速度或者停留时间（单位：秒），范围：0H ~ FH
     */
    private int speedOrDuration;

    @Getter
    public enum CruiseType {
        START_CRUISE(0x88),
        STOP_CRUISE(0x00),
        ADD_CRUISE_POINT(0x84),
        DEL_CRUISE_POINT(0x85),
        SET_CRUISE_SPEED(0x86),
        SET_CRUISE_DURATION(0x87);

        private int bit4;

        CruiseType(int bit4) {
            this.bit4 = bit4;
        }
    }
}
