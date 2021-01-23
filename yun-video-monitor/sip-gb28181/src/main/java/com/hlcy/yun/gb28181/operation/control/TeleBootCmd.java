package com.hlcy.yun.gb28181.operation.control;


import com.hlcy.yun.gb28181.bean.api.TeleBootParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;

/**
 * 远程启动控制命令
 */
public class TeleBootCmd extends AbstractControlCmd<TeleBootParams> {

    public TeleBootCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(TeleBootParams teleBootParams) {
        return "<TeleBoot>Boot</TeleBoot>";
    }

}
