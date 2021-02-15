package com.hlcy.yun.gb28181.service.command.control;


import com.hlcy.yun.gb28181.service.params.control.GuardControlParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.Operation;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.DefaultMessageResponseProcessor;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.MessageResponseProcessor;

import javax.sip.ResponseEvent;
import javax.sip.message.Request;

import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.getCallId;

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

    @Override
    protected void cacheFlowContext(GuardControlParams guardControlParams, Request request) {
        final FlowContext flowContext = new FlowContext(Operation.GUARD, guardControlParams, new DefaultMessageResponseProcessor());
        FlowContextCacheUtil.put(getCallId(request), flowContext);
    }
}
