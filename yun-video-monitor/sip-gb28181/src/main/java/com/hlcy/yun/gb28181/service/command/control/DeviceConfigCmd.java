package com.hlcy.yun.gb28181.service.command.control;

import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.params.control.DeviceConfigControlParams;

/**
 * 设备配置控制命令
 */
public class DeviceConfigCmd extends AbstractControlCmd<DeviceConfigControlParams> {

    public DeviceConfigCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(DeviceConfigControlParams params) {
        final StringBuilder cmd = new StringBuilder(200);
        cmd.append("<BasicParam>");
        if (params.getDeviceName() != null && !params.getDeviceName().isEmpty()) {
            cmd.append("<Name>").append(params.getDeviceName()).append("</Name>");
        }
        if (params.getExpiration() != null) {
            cmd.append("<Expiration>").append(params.getExpiration()).append("</Expiration>");
        }
        if (params.getHeartBeatInterval() != null) {
            cmd.append("<HeartBeatInterval>").append(params.getHeartBeatInterval()).append("</HeartBeatInterval>");
        }
        if (params.getHeartBeatCount() != null) {
            cmd.append("<HeartBeatCount>").append(params.getHeartBeatCount()).append("</HeartBeatCount>");
        }
        cmd.append("</BasicParam>");

        return cmd.toString();
    }
}
