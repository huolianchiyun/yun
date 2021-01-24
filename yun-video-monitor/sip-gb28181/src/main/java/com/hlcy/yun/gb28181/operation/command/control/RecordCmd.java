package com.hlcy.yun.gb28181.operation.command.control;


import com.hlcy.yun.gb28181.operation.params.RecordParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;

public class RecordCmd extends AbstractControlCmd<RecordParams> {

    public RecordCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(RecordParams recordParams) {
        return "<RecordCmd>" + recordParams.getOperate() + "</RecordCmd>";
    }
}
