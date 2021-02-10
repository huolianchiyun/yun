package com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify.broadcast;

import com.hlcy.yun.gb28181.bean.BroadcastResponse;
import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowResponseProcessor;
import com.hlcy.yun.gb28181.sip.biz.ResponseSender;
import gov.nist.javax.sip.header.ContentType;
import lombok.extern.slf4j.Slf4j;

import javax.sdp.SSRC;
import javax.sdp.SdpException;
import javax.sdp.SessionDescription;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.header.ContentTypeHeader;
import javax.sip.message.Message;
import javax.sip.message.Request;
import javax.sip.message.Response;

import static com.hlcy.yun.gb28181.sip.message.factory.SipResponseFactory.buildResponse;

@Slf4j
public class MediaInviteResponseProcessor extends FlowResponseProcessor {

    @Override
    protected void process(ResponseEvent event, FlowContext context) throws SdpException {
        SessionDescription mediaSdp = extractSessionDescAndSetSessionName(event.getResponse());

        final ServerTransaction deviceTransaction = context.getServerTransaction(VoiceSession.SIP_DEVICE_SESSION);
        final Request deviceInviteRequest = deviceTransaction.getRequest();
        SessionDescription deviceSdp = extractSessionDescAndSetSessionName(deviceInviteRequest);

        final SSRC ssrc = deviceSdp.getSSRC();
        mediaSdp.setSSRC(ssrc);
        context.setSsrc(ssrc.getValue());

        DeferredResultHolder.setDeferredResultForRequest(
                DeferredResultHolder.CALLBACK_CMD_VOICE + context.getOperationalParams().getChannelId(),
                new BroadcastResponse(
                        ssrc.getValue(),
                        context.getProperties().getMediaIp(),
                        mediaSdp.toString(),
                        deviceSdp.toString()
                ));

        final Response inviteResponse2Device = buildResponse(
                Response.OK,
                deviceInviteRequest,
                (ContentTypeHeader) deviceInviteRequest.getHeader(ContentType.NAME),
                mediaSdp,
                true);
        ResponseSender.sendResponse(deviceTransaction, inviteResponse2Device);

        FlowContextCacheUtil.setNewKey(getCallId(event.getResponse()), getCallId(inviteResponse2Device));
    }

    private SessionDescription extractSessionDescAndSetSessionName(Message message) throws SdpException {
        return getSessionDescription(getMessageBodyByStr(message));
    }
}
