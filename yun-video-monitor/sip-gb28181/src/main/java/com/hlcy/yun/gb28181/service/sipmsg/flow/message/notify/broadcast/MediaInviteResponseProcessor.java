package com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify.broadcast;

import com.hlcy.yun.gb28181.bean.BroadcastResponse;
import com.hlcy.yun.gb28181.exception.SSRCException;
import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowResponseProcessor;
import com.hlcy.yun.gb28181.sip.biz.ResponseSender;
import com.hlcy.yun.gb28181.util.SSRCManger;
import gov.nist.javax.sdp.fields.SSRCField;
import gov.nist.javax.sip.header.ContentType;
import lombok.extern.slf4j.Slf4j;

import javax.sdp.SdpException;
import javax.sdp.SessionDescription;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.Header;
import javax.sip.message.Message;
import javax.sip.message.Request;
import javax.sip.message.Response;

import static com.hlcy.yun.gb28181.sip.message.factory.SipResponseFactory.buildResponse;

@Slf4j
public class MediaInviteResponseProcessor extends FlowResponseProcessor {

    @Override
    protected void process(ResponseEvent event, FlowContext context) throws SdpException {
        SessionDescription mediaSdp = extractSessionDescAndSetSessionName(event.getResponse());

        String ssrc = getSSRC(context);
        context.setSsrc(ssrc);
        mediaSdp.setSSRC(new SSRCField(ssrc));

        final ServerTransaction deviceTransaction = context.getServerTransaction(VoiceSession.SIP_DEVICE_SESSION);
        final Request deviceInviteRequest = deviceTransaction.getRequest();

        SessionDescription deviceSdp = extractSessionDescAndSetSessionName(deviceInviteRequest);
        deviceSdp.setSSRC(new SSRCField(ssrc));

        DeferredResultHolder.setDeferredResultForRequest(
                DeferredResultHolder.CALLBACK_CMD_VOICE + context.getOperationalParams().getChannelId(),
                new BroadcastResponse(
                        ssrc,
                        context.getProperties().getMediaIp(),
                        mediaSdp.toString(),
                        deviceSdp.toString()
                ));

        final Response inviteResponse2Device = buildResponse(
                Response.OK,
                deviceInviteRequest,
                (ContentTypeHeader) deviceInviteRequest.getHeader(ContentType.NAME),
                deviceSdp);
        ResponseSender.sendResponse(deviceTransaction, inviteResponse2Device);

        FlowContextCacheUtil.setNewKey(getCallId(event.getResponse()), getCallId(inviteResponse2Device));
    }

    private SessionDescription extractSessionDescAndSetSessionName(Message message) throws SdpException {
        return getSessionDescription(getMessageBodyByStr(message));
    }

    private String getSSRC(FlowContext context) {
        try {
            return SSRCManger.getPlayBackSSRC();
        } catch (SSRCException e) {
            log.warn(e.getMessage());
            DeferredResultHolder.setErrorDeferredResultForRequest(
                    DeferredResultHolder.CALLBACK_CMD_VOICE + context.getOperationalParams().getChannelId(), e.getMessage());
            throw e;
        }
    }
}
