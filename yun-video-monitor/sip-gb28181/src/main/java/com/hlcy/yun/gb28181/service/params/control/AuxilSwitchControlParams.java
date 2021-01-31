package com.hlcy.yun.gb28181.service.params.control;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuxilSwitchControlParams extends ControlParams {

    private SwitchType type;

    /**
     * bit5
     * 辅助开关编号,取值为 1表示雨刷控制，范围：00H ~ FFH
     */
    private int switchNum;

    @Getter
    public enum SwitchType {
        TURN_ON(0x8C), TURN_OFF(0x8D);

        private int bit4;

        SwitchType(int bit4) {
            this.bit4 = bit4;
        }
    }
}
