package com.hlcy.yun.sip.gb28181.service;

import com.hlcy.yun.sip.gb28181.bean.Device;
import com.hlcy.yun.sip.gb28181.config.GB28181Properties;
import com.hlcy.yun.sip.gb28181.message.factory.SipRequestFactory;
import com.hlcy.yun.sip.gb28181.operation.Operation;
import com.hlcy.yun.sip.gb28181.operation.flow.FlowContextCache;
import com.hlcy.yun.sip.gb28181.operation.flow.FlowContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.sip.message.Request;

import static com.hlcy.yun.sip.gb28181.client.RequestSender.sendRequest;
import static com.hlcy.yun.sip.gb28181.message.factory.SipRequestFactory.*;

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
        sendRequest(inviteMedia);
        FlowContextCache.put(getCallId(inviteMedia), new FlowContext(Operation.PLAY, device, properties));
    }

    @Override
    public void stop(String ssrc) {
        // 15:SIP服务器收到BYE消息后向媒体服务器发送BYE消息,断开消息8、9、12建立的同媒体服务器的Invite会话。
        Request byeMedia = getByeRequest(createTo("media", properties.getMediaIp(), properties.getMediaPort()),
                createFrom("sip-server", properties.getSipIp(), properties.getSipPort()),
                "UDP", new byte[0]);
        sendRequest(byeMedia);
    }
}
