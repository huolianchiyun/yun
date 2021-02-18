package com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.play;

import com.hlcy.yun.gb28181.service.params.DeviceParams;
import com.hlcy.yun.gb28181.exception.SSRCException;
import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowResponseProcessor;
import com.hlcy.yun.gb28181.sip.biz.RequestSender;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory;
import com.hlcy.yun.gb28181.ssrc.SSRCManger;
import gov.nist.javax.sdp.fields.SSRCField;
import gov.nist.javax.sdp.fields.SessionNameField;
import lombok.extern.slf4j.Slf4j;

import javax.sdp.SdpException;
import javax.sdp.SessionDescription;
import javax.sip.ClientTransaction;
import javax.sip.ResponseEvent;
import javax.sip.message.Request;
import java.nio.charset.StandardCharsets;

@Slf4j
public class MediaInviteResponseProcessor1 extends FlowResponseProcessor {

    @Override
    protected void process(ResponseEvent event, FlowContext context) throws SdpException {

        final DeviceParams playParams = context.getOperationalParams();
        GB28181Properties properties = context.getProperties();

        SessionDescription sessionDescription = extractSessionDescAndSetSessionName(event);

        String ssrc = getSSRC(context);
        sessionDescription.setSSRC(new SSRCField(ssrc));

        Request inviteRequest2device = SipRequestFactory.getInviteRequest(
                SipRequestFactory.createTo(playParams.getChannelId(), playParams.getDeviceIp(), playParams.getDevicePort()),
                SipRequestFactory.createFrom(properties.getSipId(), properties.getSipIp(), properties.getSipPort()),
                playParams.getDeviceTransport(),
                sessionDescription.toString().getBytes(StandardCharsets.UTF_8));

        final ClientTransaction clientTransaction = RequestSender.sendRequest(inviteRequest2device);

        context.setSsrc(ssrc);
        context.put(PlaySession.SIP_DEVICE_SESSION, clientTransaction);

        FlowContextCacheUtil.setNewKey(getCallId(event.getResponse()), SipRequestFactory.getCallId(inviteRequest2device));
    }

    private SessionDescription extractSessionDescAndSetSessionName(ResponseEvent event) throws SdpException {
        SessionDescription sessionDescription = getSessionDescription(getMessageBodyByStr(event.getResponse()));
        sessionDescription.setSessionName(new SessionNameField("Play"));
        return sessionDescription;
    }

    private String getSSRC(FlowContext context) {
        try {
            return SSRCManger.getPlaySSRC();
        } catch (SSRCException e) {
            log.warn(e.getMessage());
            DeferredResultHolder.setErrorDeferredResultForRequest(context.getOperationalParams().getCallbackKey(), e.getMessage());
            throw e;
        }
    }
}
