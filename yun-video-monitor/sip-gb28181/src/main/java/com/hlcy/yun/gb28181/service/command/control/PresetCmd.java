package com.hlcy.yun.gb28181.service.command.control;

import com.hlcy.yun.gb28181.service.params.PresetParams;
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
        final StringBuilder builder = getBit123CmdTemplate();

        // 字节4
        final int bit4 = presetParams.getType().getBit4();
        builder.append(String.format("%02X", bit4), 0, 2);
        // 字节5
        int bit5 = 0x00;
        builder.append(String.format("%02X", bit5), 0, 2);
        // 字节6
        final int bit6 = presetParams.getPresetIndex();
        builder.append(String.format("%02X", bit6), 0, 2);
        // 字节7
        int bit7 = 0X00;
        builder.append(String.format("%X", bit7), 0, 2);
        // 字节8:校验码,为前面的第1~7字节的算术和的低8位,即算术和对256取模后的结果。
        // 字节8=(字节1+字节2+字节3+字节4+字节5+字节6+字节7)%256。
        int checkCode = (bit1 + bit2 + bit3 + bit4 + bit5 + bit6 + bit7) % 0X100;
        builder.append(String.format("%02X", checkCode), 0, 2);
        return builder.toString();
    }
}
