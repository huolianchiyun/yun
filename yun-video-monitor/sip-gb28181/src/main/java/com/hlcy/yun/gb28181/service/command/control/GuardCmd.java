package com.hlcy.yun.gb28181.service.command.control;


import com.hlcy.yun.gb28181.service.params.GuardParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;

/**
 * 报警布防/撤防命令
 */
public class GuardCmd extends AbstractControlCmd<GuardParams> {

    public GuardCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(GuardParams guardParams) {
        return "<GuardCmd>" + guardParams.getOperate() + "</GuardCmd>";
    }
}
