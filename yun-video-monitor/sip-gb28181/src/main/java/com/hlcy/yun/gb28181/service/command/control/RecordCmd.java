package com.hlcy.yun.gb28181.service.command.control;


import com.hlcy.yun.gb28181.service.params.control.RecordControlParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;

public class RecordCmd extends AbstractControlCmd<RecordControlParams> {

    public RecordCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(RecordControlParams recordControlParams) {
        return "<RecordCmd>" + recordControlParams.getOperate() + "</RecordCmd>";
    }
}
