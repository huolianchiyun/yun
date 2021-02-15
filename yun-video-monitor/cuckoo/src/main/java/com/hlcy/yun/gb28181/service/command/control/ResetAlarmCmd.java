package com.hlcy.yun.gb28181.service.command.control;


import com.hlcy.yun.gb28181.service.params.control.ResetAlarmControlParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.Operation;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.DefaultMessageResponseProcessor;

import javax.sip.message.Request;

import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.getCallId;

/**
 * 告警复位命令
 */
public class ResetAlarmCmd extends AbstractControlCmd<ResetAlarmControlParams> {
    public ResetAlarmCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(ResetAlarmControlParams resetAlarmControlParams) {
        return "<AlarmCmd>" + "ResetAlarm" + "</AlarmCmd>";
    }

    @Override
    protected void cacheFlowContext(ResetAlarmControlParams resetAlarmControlParams, Request request) {
        final FlowContext flowContext = new FlowContext(Operation.RESET_ALARM, resetAlarmControlParams, new DefaultMessageResponseProcessor());
        FlowContextCacheUtil.put(getCallId(request), flowContext);
    }
}
