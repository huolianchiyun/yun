package com.hlcy.yun.gb28181.service.command.query;

import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.params.query.QueryParams;

/**
 * 设备录像（历史视频）查询
 */
public class RecordInfoQueryCmd extends AbstractQueryCmd<QueryParams> {
    public RecordInfoQueryCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(QueryParams params) {


        return null;
    }
}
