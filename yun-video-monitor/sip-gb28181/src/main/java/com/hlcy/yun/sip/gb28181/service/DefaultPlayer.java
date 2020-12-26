package com.hlcy.yun.sip.gb28181.service;

import com.hlcy.yun.sip.gb28181.bean.Device;
import com.hlcy.yun.sip.gb28181.config.GB28181Properties;
import com.hlcy.yun.sip.gb28181.operation.Operation;
import com.hlcy.yun.sip.gb28181.operation.flow.FlowContextCache;
import com.hlcy.yun.sip.gb28181.operation.flow.FlowContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.sip.message.Request;
import static com.hlcy.yun.sip.gb28181.client.RequestSender.sendRequest;
import static com.hlcy.yun.sip.gb28181.message.factory.SipRequestFactory.getCallId;
import static com.hlcy.yun.sip.gb28181.message.factory.SipRequestFactory.getInviteRequest;

@Service
@RequiredArgsConstructor
public class DefaultPlayer implements Player {
    private final GB28181Properties properties;

    @Override
    public void play(Device device) {
        Request inviteMedia = getInviteRequest(null, null, null, null, null);
        sendRequest(inviteMedia);
        FlowContextCache.put(getCallId(inviteMedia), new FlowContext(Operation.PLAY, device));

    }

    @Override
    public void stop(String ssrc) {
        Request byeMedia = getInviteRequest(null, null, null, null, null);
        sendRequest(byeMedia);
    }
}
