package com.hlcy.yun.gb28181.service.params.control;

import lombok.Getter;
import lombok.Setter;

/**
 * 预设位控制参数，详情见：GB/T 28181-2016
 * A.3.4预置位指令
 */
@Getter
@Setter
public class PresetControlParams extends ControlParams {

    private PresetType type;
    /**
     * bit6
     * 预置位数目最大为 255,0 号预留
     */
    private int presetIndex;

    @Getter
    public enum PresetType {
        SET_PRESET(0x81), CALL_PRESET(0x82), DEL_PRESET(0x83);

        private int bit4;

        PresetType(int bit4) {
            this.bit4 = bit4;
        }
    }
}
