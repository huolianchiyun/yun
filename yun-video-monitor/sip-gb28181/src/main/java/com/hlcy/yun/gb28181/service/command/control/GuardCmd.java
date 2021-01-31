package com.hlcy.yun.gb28181.service.command.control;


import com.hlcy.yun.gb28181.service.params.control.GuardControlParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;

/**
 * 报警布防/撤防命令
 */
public class GuardCmd extends AbstractControlCmd<GuardControlParams> {

    public GuardCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(GuardControlParams guardControlParams) {
        return "<GuardCmd>" + guardControlParams.getOperate() + "</GuardCmd>";
    }
}
