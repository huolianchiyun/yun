package com.hlcy.yun.gb28181.service.command.query;

import com.hlcy.yun.gb28181.service.params.query.CatalogQueryParams;
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
