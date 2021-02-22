package com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.playback;

import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowResponseProcessor;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.sip.biz.RequestSender;
import com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory;

import javax.sip.ClientTransaction;
import javax.sip.ResponseEvent;
import javax.sip.message.Request;

public class MediaByeResponseProcessor1 extends FlowResponseProcessor {
    @Override
    protected void process(ResponseEvent event, FlowContext context) {
        final ClientTransaction clientTransaction = context.getClientTransaction(PlaybackSession.SIP_MEDIA_SESSION_1);
        final Request bye = SipRequestFactory.getByeRequest(clientTransaction);
        RequestSender.sendByeRequest(bye, clientTransaction);

        FlowContextCacheUtil.setNewKey(getCallId(event.getResponse()), SipRequestFactory.getCallId(bye));
    }
}
