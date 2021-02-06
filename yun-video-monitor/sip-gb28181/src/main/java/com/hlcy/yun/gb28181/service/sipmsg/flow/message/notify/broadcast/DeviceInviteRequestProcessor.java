package com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify.broadcast;

import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.params.DeviceParams;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowRequestProcessor;

import javax.sip.ClientTransaction;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.message.Request;

import static com.hlcy.yun.gb28181.sip.biz.RequestSender.sendRequest;
import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.*;

public class DeviceInviteRequestProcessor extends FlowRequestProcessor {

    @Override
    protected void process(RequestEvent event, FlowContext context) {
        final GB28181Properties properties = context.getProperties();
        final DeviceParams params = context.getOperationalParams();
        final ServerTransaction serverTransaction = event.getServerTransaction();
        context.put(VoiceSession.SIP_DEVICE_SESSION, serverTransaction);

        Request invite2Media = getInviteRequest(
                createTo(properties.getMediaId(), properties.getMediaIp(), properties.getMediaVideoPort()),
                createFrom(properties.getSipId(), properties.getSipIp(), properties.getSipPort()),
                params.getDeviceTransport(),
                serverTransaction.getRequest().getRawContent());
        final ClientTransaction clientTransaction = sendRequest(invite2Media);

        context.put(VoiceSession.SIP_MEDIA_SESSION, clientTransaction);
        FlowContextCacheUtil.put(getCallId(invite2Media), context);
    }
}
