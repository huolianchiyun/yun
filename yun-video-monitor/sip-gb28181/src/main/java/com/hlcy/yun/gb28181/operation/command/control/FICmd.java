package com.hlcy.yun.gb28181.operation.command.control;

import com.hlcy.yun.gb28181.operation.params.FIParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;

/**
 * 光圈控制和聚焦控制指令
 */
public class FICmd extends AbstractControlCmd<FIParams> {
    public FICmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(FIParams params) {
        return "<PTZCmd>" + buildFICmd(params) + "</PTZCmd>";
    }

    private String buildFICmd(FIParams params) {
        final StringBuilder builder = getBit123CmdTemplate();

        // 字节4
        int cmdCode = getCmdCode(params.getFocus(), params.getIris());
        builder.append(String.format("%02X", cmdCode), 0, 2);
        // 字节5
        final int bit5 = params.getFocusSpeed();
        builder.append(String.format("%02X", bit5), 0, 2);
        // 字节6
        final int bit6 = params.getIrisSpeed();
        builder.append(String.format("%02X", bit6), 0, 2);
        // 字节7
        builder.append(String.format("%X", 0), 0, 1).append("0");
        // 字节8:校验码,为前面的第1~7字节的算术和的低8位,即算术和对256取模后的结果。
        // 字节8=(字节1+字节2+字节3+字节4+字节5+字节6+字节7)%256。
        int checkCode = (bit1 + bit2 + bit3 + cmdCode + bit5 + bit6 + (0 & 0XF0)) % 0X100;
        builder.append(String.format("%02X", checkCode), 0, 2);
        return builder.toString();
    }

    private int getCmdCode(int focus, int iris) {
        int cmdCode = 0x20; // 0010 0000 bit6 设置成 1
        if (focus == 1) {
            cmdCode |= 0x01;  // 远 bit0
        } else if (focus == 2) {
            cmdCode |= 0x02;  // 近 bit1
        }
        if (iris == 1) {
            cmdCode |= 0x04;  // 放大 bit2
        } else if (iris == 2) {
            cmdCode |= 0x08;  // 缩小 bit3
        }
        return cmdCode;
    }
}
