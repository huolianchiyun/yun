package com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.playback;

import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowResponseProcessor;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.sip.biz.RequestSender;
import com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory;

import javax.sip.ClientTransaction;
import javax.sip.ResponseEvent;
import javax.sip.message.Request;

public class DeviceInviteResponseProcessor extends FlowResponseProcessor {

    @Override
    protected void process(ResponseEvent event, FlowContext context) {
        final ClientTransaction mediaTransaction = context.getClientTransaction(PlaybackSession.SIP_MEDIA_SESSION_1);
        final Request ackRequest4Media = SipRequestFactory.getAckRequest(mediaTransaction, getMessageBodyByByteArr(event.getResponse()));
        RequestSender.sendAckRequest(ackRequest4Media, mediaTransaction);

        final ClientTransaction deviceTransaction = context.getClientTransaction(PlaybackSession.SIP_DEVICE_SESSION);
        final Request ackRequest4Device = SipRequestFactory.getAckRequest(deviceTransaction);
        RequestSender.sendAckRequest(ackRequest4Device, deviceTransaction);

        final Request inviteRequest4Device = deviceTransaction.getRequest();
        final GB28181Properties properties = context.getProperties();
        Request inviteRequest2media = SipRequestFactory.getInviteRequest(
                SipRequestFactory.createTo(properties.getMediaId(), properties.getMediaIp(), properties.getMediaVideoPort()),
                SipRequestFactory.createFrom(properties.getSipId(), properties.getSipIp(), properties.getSipPort()),
                context.getOperationalParams().getDeviceTransport(),
                inviteRequest4Device.getRawContent());

        final ClientTransaction clientTransaction = RequestSender.sendRequest(inviteRequest2media);

        context.put(PlaybackSession.SIP_MEDIA_SESSION_2, clientTransaction);
        FlowContextCacheUtil.put(SipRequestFactory.getCallId(inviteRequest2media), context);
        FlowContextCacheUtil.putSerialize(context.getSsrc(), context);

    }
}
