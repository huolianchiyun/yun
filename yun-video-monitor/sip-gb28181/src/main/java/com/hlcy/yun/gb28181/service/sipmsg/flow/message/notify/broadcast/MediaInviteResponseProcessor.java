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
import lombok.extern.slf4j.Slf4j;

import javax.sdp.SdpException;
import javax.sdp.SessionDescription;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.message.Response;

import static com.hlcy.yun.gb28181.sip.message.factory.SipResponseFactory.buildResponse;

@Slf4j
public class MediaInviteResponseProcessor extends FlowResponseProcessor {

    @Override
    protected void process(ResponseEvent event, FlowContext context) throws SdpException {
        SessionDescription sdp = extractSessionDescAndSetSessionName(event);

        String ssrc = getSSRC(context);
        context.setSsrc(ssrc);
        sdp.setSSRC(new SSRCField(ssrc));

        DeferredResultHolder.setDeferredResultForRequest(
                DeferredResultHolder.CALLBACK_CMD_VOICE + context.getOperationalParams().getChannelId(),
                new BroadcastResponse(ssrc, context.getProperties().getMediaIp(), sdp.toString()));

        final ServerTransaction deviceTransaction = context.getServerTransaction(VoiceSession.SIP_DEVICE_SESSION);
        final Response inviteResponse2Device = buildResponse(Response.OK, deviceTransaction.getRequest());
        ResponseSender.sendResponse(deviceTransaction, inviteResponse2Device);

        FlowContextCacheUtil.setNewKey(getCallId(event.getResponse()), getCallId(inviteResponse2Device));
    }

    private SessionDescription extractSessionDescAndSetSessionName(ResponseEvent event) throws SdpException {
        return getSessionDescription(getMessageBodyByStr(event.getResponse()));
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
