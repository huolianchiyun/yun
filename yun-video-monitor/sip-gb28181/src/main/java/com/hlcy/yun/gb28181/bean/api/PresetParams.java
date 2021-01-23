package com.hlcy.yun.gb28181.bean.api;

import lombok.Getter;
import lombok.Setter;

/**
 * 预设位控制参数，详情见：GB/T 28181-2016
 * A.3.4预置位指令
 */
@Getter
@Setter
public class PresetParams extends DeviceParams {

    private PresetType type;
    /**
     * 预置位数目最大为 255,0 号预留
     */
    private int presetIndex;

    @Getter
    public enum PresetType {
        SETUP(0x81), CALL(0x82), DEL(0x83);

        private int code;

        PresetType(int code) {
            this.code = code;
        }
    }
}
