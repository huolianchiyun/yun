package com.hlcy.yun.gb28181.operation.control;

import com.hlcy.yun.gb28181.bean.api.ScanParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;

/**
 * 扫描指令
 */
public class ScanCmd extends AbstractControlCmd<ScanParams> {
    public ScanCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(ScanParams params) {


        return null;
    }
}
