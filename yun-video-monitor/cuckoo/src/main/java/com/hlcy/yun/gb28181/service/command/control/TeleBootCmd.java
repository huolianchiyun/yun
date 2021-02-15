package com.hlcy.yun.gb28181.service.command.control;


import com.hlcy.yun.gb28181.service.params.control.TeleBootControlParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.Operation;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.DefaultMessageResponseProcessor;

import javax.sip.message.Request;

import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.getCallId;

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

    @Override
    protected void cacheFlowContext(TeleBootControlParams teleBootControlParams, Request request) {
        final FlowContext flowContext = new FlowContext(Operation.TELE_BOOT, teleBootControlParams, new DefaultMessageResponseProcessor());
        FlowContextCacheUtil.put(getCallId(request), flowContext);
    }
}
