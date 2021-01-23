package com.hlcy.yun.gb28181.operation.query;

import com.hlcy.yun.gb28181.bean.api.QueryParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;

/**
 * 预置位查询指令
 */
public class PresetQueryCmd extends AbstractQueryCmd<QueryParams> {
    public PresetQueryCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(QueryParams params) {


        return null;
    }
}
