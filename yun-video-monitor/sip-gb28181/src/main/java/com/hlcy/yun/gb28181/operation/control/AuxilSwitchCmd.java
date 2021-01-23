package com.hlcy.yun.gb28181.operation.control;

import com.hlcy.yun.gb28181.bean.api.AuxilSwitchParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;

/**
 * 辅助开关控制指令
 */
public class AuxilSwitchCmd extends AbstractControlCmd<AuxilSwitchParams> {
    public AuxilSwitchCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(AuxilSwitchParams auxilSwitchParams) {
        return null;
    }
}
