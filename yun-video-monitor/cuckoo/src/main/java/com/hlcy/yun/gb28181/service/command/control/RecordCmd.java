package com.hlcy.yun.gb28181.service.command.control;

import com.hlcy.yun.gb28181.service.sipmsg.MANSCDPXmlParser;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.DefaultMessageRequestProcessor;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.DefaultMessageResponseProcessor;
import com.hlcy.yun.gb28181.service.params.control.RecordControlParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.Operation;

import javax.sip.message.Request;

import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.getCallId;

public class RecordCmd extends AbstractControlCmd<RecordControlParams> {

    public RecordCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(RecordControlParams recordControlParams) {
        return "<RecordCmd>" + recordControlParams.getOperate() + "</RecordCmd>";
    }

    @Override
    protected void cacheFlowContext(RecordControlParams recordControlParams, Request request) {
        final FlowContext flowContext = new FlowContext(
                Operation.RECORD,
                recordControlParams,
                new DefaultMessageRequestProcessor(),
                new DefaultMessageResponseProcessor().setResponseCallback(false));
        FlowContextCacheUtil.put(getCallId(request), flowContext);
        FlowContextCacheUtil.put(MANSCDPXmlParser.getSN(request), flowContext);
    }
}
