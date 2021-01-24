package com.hlcy.yun.gb28181.operation.command.query;

import com.hlcy.yun.gb28181.operation.params.DeviceInfoQueryParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;

public class DeviceInfoCommand extends AbstractQueryCmd<DeviceInfoQueryParams> {
    public DeviceInfoCommand(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(DeviceInfoQueryParams params) {
        return "";
    }
}
