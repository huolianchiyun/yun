package com.hlcy.yun.gb28181.service.command.query;

import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.params.query.QueryParams;

public class AlarmQueryCmd extends AbstractQueryCmd<QueryParams> {

    public AlarmQueryCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(QueryParams queryParams) {
        return null;
    }
}
