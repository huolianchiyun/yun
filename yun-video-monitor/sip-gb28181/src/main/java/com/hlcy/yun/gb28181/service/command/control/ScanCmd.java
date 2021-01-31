package com.hlcy.yun.gb28181.service.command.control;

import com.hlcy.yun.gb28181.service.params.control.ScanControlParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;

/**
 * 扫描指令
 */
public class ScanCmd extends AbstractControlCmd<ScanControlParams> {
    public ScanCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(ScanControlParams params) {
        return "<PTZCmd>" + buildScanCmd(params) + "</PTZCmd>";
    }

    private String buildScanCmd(ScanControlParams params) {
        final StringBuilder builder = getBit123CmdTemplate();

        final ScanControlParams.ScanType type = params.getType();
        // 字节4
        int bit4 = type.getBit4();
        builder.append(String.format("%02X", bit4), 0, 2);
        // 字节5
        int bit5 = params.getScanGroupNum();
        builder.append(String.format("%02X", bit5), 0, 2);
        // 字节6
        int bit6 = type.getBit6();
        builder.append(String.format("%02X", bit6), 0, 2);
        // 字节7
        int bit7 = type == ScanControlParams.ScanType.SET_AUTO_SCAN_SPEED ? params.getScanSpeed() : 0x00;
        builder.append(String.format("%X", bit7), 0, 1).append("0");
        // 字节8:校验码,为前面的第1~7字节的算术和的低8位,即算术和对256取模后的结果。
        // 字节8=(字节1+字节2+字节3+字节4+字节5+字节6+字节7)%256。
        int checkCode = (bit1 + bit2 + bit3 + bit4 + bit5 + bit6 + (bit7 << 4 & 0XF0)) % 0X100;
        builder.append(String.format("%02X", checkCode), 0, 2);
        return builder.toString();
    }
}
