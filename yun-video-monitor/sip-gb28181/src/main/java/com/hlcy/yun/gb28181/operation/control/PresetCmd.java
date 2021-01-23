package com.hlcy.yun.gb28181.operation.control;

import com.hlcy.yun.gb28181.bean.api.PresetParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;

/**
 * 预置位指令
 */
public class PresetCmd extends AbstractControlCmd<PresetParams> {
    public PresetCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(PresetParams presetParams) {
        return "<PTZCmd>" + buildPresetCmd(presetParams) + "</PTZCmd>";
    }

    private String buildPresetCmd(PresetParams presetParams) {
        return doBuildPresetCmd(presetParams.getType(), presetParams.getPresetIndex());
    }

    private String doBuildPresetCmd(PresetParams.PresetType type, int presetIndex) {
        StringBuilder builder = new StringBuilder("A50F01"); // 字节1、2、3
        // 字节4
        builder.append(String.format("%02X", type.getCode()), 0, 2);
        // 字节5
        builder.append(String.format("%02X", 0), 0, 2);
        // 字节6
        builder.append(String.format("%02X", presetIndex), 0, 2);
        // 字节7
        builder.append(String.format("%X", 0), 0, 1).append("0");
        // 字节8:校验码,为前面的第1~7字节的算术和的低8位,即算术和对256取模后的结果。
        // 字节8=(字节1+字节2+字节3+字节4+字节5+字节6+字节7)%256。
        int checkCode = (0XA5 + 0X0F + 0X01 + type.getCode() + 0x00 + presetIndex + (0x00 << 4 & 0XF0)) % 0X100;
        builder.append(String.format("%02X", checkCode), 0, 2);
        return builder.toString();
    }
}
