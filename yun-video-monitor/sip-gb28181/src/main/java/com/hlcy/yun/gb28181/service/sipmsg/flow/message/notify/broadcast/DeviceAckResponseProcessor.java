package com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify.broadcast;

import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowResponseProcessor;
import com.hlcy.yun.gb28181.sip.biz.RequestSender;
import com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory;

import javax.sip.ClientTransaction;
import javax.sip.ResponseEvent;
import javax.sip.message.Request;

public class DeviceAckResponseProcessor extends FlowResponseProcessor {

    @Override
    protected void process(ResponseEvent event, FlowContext context) {

        final ClientTransaction mediaTransaction = context.getClientTransaction(VoiceSession.SIP_MEDIA_SESSION);
        final Request ackRequest4Media = SipRequestFactory.getAckRequest(mediaTransaction);
        RequestSender.sendAckRequest(ackRequest4Media, mediaTransaction);
        FlowContextCacheUtil.setNewKey(getCallId(event.getResponse()), context.getSsrc());
    }
}
