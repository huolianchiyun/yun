package com.hlcy.yun.gb28181.operation.control;

import com.hlcy.yun.gb28181.bean.api.CruiseParams;
import com.hlcy.yun.gb28181.bean.api.FIParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;

/**
 * 巡航指令
 */
public class CruiseCmd extends AbstractControlCmd<CruiseParams> {
    public CruiseCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(CruiseParams params) {





        return null;
    }
}
