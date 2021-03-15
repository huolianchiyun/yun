package com.hlcy.yun.gb28181.service.command.query;

import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.params.query.ConfigDownloadQueryParams;

public class ConfigDownloadQueryCmd extends AbstractQueryCmd<ConfigDownloadQueryParams> {
    public ConfigDownloadQueryCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(ConfigDownloadQueryParams params) {
        return "<ConfigType>" + params.getConfigType() + "</ConfigType>";
    }
}
