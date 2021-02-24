package com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.download;

import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.exception.SSRCException;
import com.hlcy.yun.gb28181.service.params.player.DownloadParams;
import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowResponseProcessor;
import com.hlcy.yun.gb28181.sip.biz.RequestSender;
import com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory;
import com.hlcy.yun.gb28181.ssrc.SSRCManger;
import gov.nist.javax.sdp.fields.SSRCField;
import gov.nist.javax.sdp.fields.SessionNameField;
import gov.nist.javax.sdp.fields.TimeField;
import gov.nist.javax.sdp.fields.URIField;
import lombok.extern.slf4j.Slf4j;

import javax.sdp.Attribute;
import javax.sdp.SdpException;
import javax.sdp.SessionDescription;
import javax.sip.ClientTransaction;
import javax.sip.ResponseEvent;
import javax.sip.message.Request;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Vector;

@Slf4j
public class MediaInviteResponseProcessor1 extends FlowResponseProcessor {

    @Override
    protected void process(ResponseEvent event, FlowContext context) throws SdpException {
        SessionNameField sessionNameField = new SessionNameField("Download");
        SessionDescription sessionDescription = getSessionDescription(getMessageBodyByStr(event.getResponse()));
        sessionDescription.setSessionName(sessionNameField);

        final DownloadParams params = (DownloadParams) context.getOperationalParams();
        final TimeField timeField = new TimeField();
        timeField.setStartTime(params.getStartTimestamp());
        timeField.setStopTime(params.getEndTimestamp());
        sessionDescription.setTimeDescriptions(new Vector<>(Collections.singletonList(timeField)));

        final URIField uriField = new URIField();
        uriField.setURI(params.getChannelId() + ":" + params.getDownloadType());
        sessionDescription.setURI(uriField);

        sessionDescription.setAttribute("downloadspeed", String.valueOf(params.getDownloadSpeed()));

        final String ssrc = getSSRC(context);
        sessionDescription.setSSRC(new SSRCField(ssrc));

        GB28181Properties properties = context.getProperties();
        Request inviteRequest2device = SipRequestFactory.getInviteRequest(
                SipRequestFactory.createTo(params.getChannelId(), params.getDeviceIp(), params.getDevicePort()),
                SipRequestFactory.createFrom(properties.getSipId(), properties.getSipIp(), properties.getSipPort()),
                params.getDeviceTransport(),
                sessionDescription.toString().getBytes(StandardCharsets.UTF_8));

        final ClientTransaction clientTransaction = RequestSender.sendRequest(inviteRequest2device);

        context.setSsrc(ssrc);
        context.put(DownloadSession.SIP_DEVICE_SESSION, clientTransaction);
        FlowContextCacheUtil.setNewKey(getCallId(event.getResponse()), SipRequestFactory.getCallId(inviteRequest2device));
    }

    private String getSSRC(FlowContext context) {
        try {
            return SSRCManger.getPlayBackSSRC();
        } catch (SSRCException e) {
            log.warn(e.getMessage());
            DeferredResultHolder.setErrorDeferredResultForRequest(
                     context.getOperationalParams().getCallbackKey(), e.getMessage());
            throw e;
        }
    }
}
