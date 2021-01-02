package com.hlcy.yun.gb28181.service;

import com.hlcy.yun.gb28181.bean.Device;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.operation.Operation;
import com.hlcy.yun.gb28181.operation.flow.FlowContextCache;
import com.hlcy.yun.gb28181.operation.flow.FlowContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.sip.ClientTransaction;
import javax.sip.message.Request;

import static com.hlcy.yun.gb28181.sip.client.RequestSender.sendRequest;
import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.*;
import static com.hlcy.yun.gb28181.operation.flow.play.PlaySession.SIP_MEDIA_SESSION_1;
import static com.hlcy.yun.gb28181.operation.flow.play.PlaySession.SIP_MEDIA_SESSION_2;

@Service
@RequiredArgsConstructor
public class DefaultPlayer implements Player {
    private final GB28181Properties properties;

    @Override
    public void play(Device device) {
        // 2:向媒体服务器发送Invite消息,此消息不携带SDP消息体。
        Request inviteMedia = getInviteRequest(createTo("media", properties.getMediaIp(), properties.getMediaPort()),
                createFrom("sip-server", properties.getSipIp(), properties.getSipPort()),
                device.getTransport(), new byte[0]);
        final ClientTransaction clientTransaction = sendRequest(inviteMedia);

        final FlowContext flowContext = new FlowContext(Operation.PLAY, device, properties);
        flowContext.put(SIP_MEDIA_SESSION_1, clientTransaction);
        FlowContextCache.put(getCallId(inviteMedia), flowContext);
    }

    @Override
    public void stop(String ssrc) {
        // 15:SIP服务器收到BYE消息后向媒体服务器发送BYE消息,断开消息8、9、12建立的同媒体服务器的Invite会话。
        final FlowContext context = FlowContextCache.get(ssrc);
        final ClientTransaction clientTransaction = context.get(SIP_MEDIA_SESSION_2);
        final Request bye = getByeRequest(clientTransaction);
        sendRequest(bye);

        FlowContextCache.setNewKey(ssrc, getCallId(bye));
    }
}
