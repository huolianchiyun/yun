package com.hlcy.yun.gb28181.operation.command.query;

import com.hlcy.yun.gb28181.operation.params.QueryParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;

/**
 * 预置位查询指令
 */
public class PresetCommand extends AbstractQueryCmd<QueryParams> {
    public PresetCommand(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(QueryParams params) {


        return null;
    }
}
