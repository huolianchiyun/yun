package com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify.broadcast;

import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowRequestProcessor;
import com.hlcy.yun.gb28181.sip.biz.RequestSender;
import com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory;

import javax.sip.ClientTransaction;
import javax.sip.RequestEvent;
import javax.sip.message.Request;

public class DeviceAckRequestProcessor extends FlowRequestProcessor {

    @Override
    protected void process(RequestEvent event, FlowContext context) {
        final ClientTransaction mediaTransaction = context.getClientTransaction(VoiceSession.SIP_MEDIA_SESSION);
        final Request ackRequest4Media = SipRequestFactory.getAckRequest(mediaTransaction);
        RequestSender.sendAckRequest(ackRequest4Media, mediaTransaction);
        // 为以 VoiceBroadcastNotifyCmd.stop(ssrc) 方式关流预留资源
        FlowContextCacheUtil.setNewKey(getCallId(event.getRequest()), context.getSsrc());
        // 为设备主动 bye 时，预留资源
        FlowContextCacheUtil.put(getCallId(event.getRequest()), context);
    }
}
