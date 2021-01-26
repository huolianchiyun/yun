package com.hlcy.yun.gb28181.operation.command.query;

import com.hlcy.yun.gb28181.operation.params.CatalogQueryParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;

public class CatalogQueryCmd extends AbstractQueryCmd<CatalogQueryParams> {
    public CatalogQueryCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(CatalogQueryParams params) {
        return "";
    }
}
