package com.hlcy.yun.gb28181.operation.command.control;

import com.hlcy.yun.gb28181.operation.params.PtzParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;

/**
 * 云台控制命令
 */
public class PtzCmd extends AbstractControlCmd<PtzParams> {

    public PtzCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(PtzParams ptzParams) {
        return "<PTZCmd>" + buildPTZCmd(ptzParams) + "</PTZCmd>";
    }

    private String buildPTZCmd(PtzParams ptzParams) {
        return doBuildPTZCmd(
                ptzParams.getPan(),
                ptzParams.getTilt(),
                ptzParams.getZoom(),
                ptzParams.getPanSpeed(),
                ptzParams.getTiltSpeed(),
                ptzParams.getZoomSpeed());
    }

    /**
     * 构建云台控制指令
     * 详情见：GB/T 28181-2016 的 A.3.1 指令格式和 A.3.2PTZ指令
     *
     * @param pan       镜头左移右移 0:停止 1:左移 2:右移
     * @param tilt      镜头上移下移 0:停止 1:上移 2:下移
     * @param zoom      镜头放大缩小 0:停止 1:缩小 2:放大
     * @param panSpeed  镜头移动速度 默认 0XFF (0-255)
     * @param tiltSpeed 镜头移动速度 默认 0XFF (0-255)
     * @param zoomSpeed 镜头缩放速度 默认 0X1 (0-15)
     */
    private String doBuildPTZCmd(int pan, int tilt, int zoom, int panSpeed, int tiltSpeed, int zoomSpeed) {
        final StringBuilder builder = getBit123CmdTemplate();
        // 字节4: 指令码
        int bit4 = getCmdCode(pan, tilt, zoom);
        builder.append(String.format("%02X", bit4), 0, 2);

        // 字节5：水平控制速度相对值
        builder.append(String.format("%02X", panSpeed), 0, 2);

        // 字节6：垂直控制速度相对值
        builder.append(String.format("%02X", tiltSpeed), 0, 2);

        // 字节7：变倍控制速度相对值
        int bit7 = zoomSpeed << 4 & 0XF0;  // 也可以用 int bit7 = zoomSpeed << 4 | 0x00;

        builder.append(String.format("%X", zoomSpeed), 0, 2);

        // 字节8:校验码,为前面的第1~7字节的算术和的低8位,即算术和对256取模后的结果。
        // 字节8=(字节1+字节2+字节3+字节4+字节5+字节6+字节7)%256。
        int checkCode = (bit1 + bit2 + bit3 + bit4 + panSpeed + tiltSpeed + bit7) % 0X100;
        builder.append(String.format("%02X", checkCode), 0, 2);

        return builder.toString();
    }

    private int getCmdCode(int pan, int tilt, int zoom) {
        int cmdCode = 0;
        if (pan == 2) {
            cmdCode |= 0x01;  // 右移 bit0
        } else if (pan == 1) {
            cmdCode |= 0x02;  // 左移 bit1
        }

        if (tilt == 2) {
            cmdCode |= 0x04;  // 下移 bit2
        } else if (tilt == 1) {
            cmdCode |= 0x08;  // 上移 bit3
        }

        if (zoom == 2) {
            cmdCode |= 0x10;  // 放大 bit4
        } else if (zoom == 1) {
            cmdCode |= 0x20;  // 缩小 bit5
        }
        return cmdCode;
    }
}
