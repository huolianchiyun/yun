package com.hlcy.yun.gb28181.operation.command.control;

import com.hlcy.yun.gb28181.operation.params.CruiseParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;

/**
 * 巡航指令
 */
public class CruiseCmd extends AbstractControlCmd<CruiseParams> {
    public CruiseCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(CruiseParams presetParams) {
        return "<PTZCmd>" + buildCruiseCmd(presetParams) + "</PTZCmd>";
    }

    private String buildCruiseCmd(CruiseParams params) {
        final StringBuilder builder = getBit123CmdTemplate();
        // 字节4
        int bit4 = params.getType().getBit4();
        builder.append(String.format("%02X", params.getType().getBit4()), 0, 2);
        // 字节5
        int bit5 = params.getGroupNum();
        builder.append(String.format("%02X", bit5), 0, 2);

        final CruiseParams.CruiseType type = params.getType();
        // 字节6
        int bit6 = CruiseParams.CruiseType.START_CRUISE == type ? 0x00 : params.getPresetIndex();
        builder.append(String.format("%02X", bit6), 0, 2);
        // 字节7
        final int bit7 = CruiseParams.CruiseType.SET_CRUISE_SPEED == type || CruiseParams.CruiseType.SET_CRUISE_DURATION == type ? params.getSpeedOrDuration() : 0x00;
        builder.append(String.format("%X", bit7), 0, 1).append("0");

        // 字节8:校验码,为前面的第1~7字节的算术和的低8位,即算术和对256取模后的结果。
        // 字节8=(字节1+字节2+字节3+字节4+字节5+字节6+字节7)%256。
        int checkCode = (bit1 + bit2 + bit3 + bit4 + bit5 + bit6 + (bit7 << 4 & 0XF0)) % 0X100;
        builder.append(String.format("%02X", checkCode), 0, 2);
        return builder.toString();
    }
}
