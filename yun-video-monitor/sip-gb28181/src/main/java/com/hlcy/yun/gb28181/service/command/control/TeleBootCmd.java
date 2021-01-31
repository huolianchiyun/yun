package com.hlcy.yun.gb28181.service.command.control;


import com.hlcy.yun.gb28181.service.params.control.TeleBootControlParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;

/**
 * 远程启动控制命令
 */
public class TeleBootCmd extends AbstractControlCmd<TeleBootControlParams> {

    public TeleBootCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(TeleBootControlParams teleBootControlParams) {
        return "<TeleBoot>Boot</TeleBoot>";
    }

}
