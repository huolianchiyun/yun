package com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.playback;

import com.hlcy.yun.gb28181.bean.PlayResponse;
import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowResponseProcessor;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.sip.biz.RequestSender;
import com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory;

import javax.sip.ClientTransaction;
import javax.sip.ResponseEvent;
import javax.sip.message.Request;


public class MediaInviteResponseProcessor2 extends FlowResponseProcessor {
    @Override
    protected void process(ResponseEvent event, FlowContext context) {
        final String ssrc = context.getSsrc();
        DeferredResultHolder.setDeferredResultForRequest(
                DeferredResultHolder.CALLBACK_CMD_PLAYBACK + context.getOperationalParams().getChannelId(),
                new PlayResponse(ssrc, context.getProperties().getMediaIp()));

        final ClientTransaction mediaTransaction = context.getClientTransaction(PlaybackSession.SIP_MEDIA_SESSION_2);
        final Request ackRequest4Media = SipRequestFactory.getAckRequest(mediaTransaction, getMessageBodyByByteArr(event.getResponse()));
        RequestSender.sendAckRequest(ackRequest4Media, mediaTransaction);
        FlowContextCacheUtil.setNewKey(getCallId(event.getResponse()), ssrc);
    }
}
