package com.hlcy.yun.gb28181.service;

import com.hlcy.yun.gb28181.bean.api.PlayParams;
import com.hlcy.yun.gb28181.bean.api.PlaybackParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.operation.Operation;
import com.hlcy.yun.gb28181.operation.flow.FlowContextCache;
import com.hlcy.yun.gb28181.operation.flow.FlowContext;
import com.hlcy.yun.gb28181.operation.flow.palyer.playback.PlaybackSession;
import com.hlcy.yun.gb28181.sip.client.RequestSender;
import com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory;
import com.hlcy.yun.gb28181.util.SSRCManger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.sip.ClientTransaction;
import javax.sip.message.Request;
import java.nio.charset.StandardCharsets;
import static com.hlcy.yun.gb28181.sip.client.RequestSender.sendRequest;
import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.*;
import static com.hlcy.yun.gb28181.operation.flow.palyer.play.PlaySession.SIP_MEDIA_SESSION_1;
import static com.hlcy.yun.gb28181.operation.flow.palyer.play.PlaySession.SIP_MEDIA_SESSION_2;

@Service
@RequiredArgsConstructor
public class DefaultPlayer implements Player {
    private final GB28181Properties properties;

    @Override
    public void play(PlayParams params) {
        // 2:向媒体服务器发送Invite消息,此消息不携带SDP消息体。
        Request inviteMedia = getInviteRequest(
                createTo(properties.getMediaId(), properties.getMediaIp(), properties.getMediaPort()),
                createFrom(properties.getSipId(), properties.getSipIp(), properties.getSipPort()),
                params.getDeviceTransport());
        final ClientTransaction clientTransaction = sendRequest(inviteMedia);

        final FlowContext flowContext = new FlowContext(Operation.PLAY, params);
        FlowContext.setProperties(properties);
        flowContext.put(SIP_MEDIA_SESSION_1, clientTransaction);
        FlowContextCache.put(getCallId(inviteMedia), flowContext);
    }

    @Override
    public void stop(String ssrc) {
        // 点播 15:SIP服务器收到BYE消息后向媒体服务器发送BYE消息,断开消息8、9、12建立的同媒体服务器的Invite会话。
        // 回放 23:SIP服务器收到 BYE 消息后向媒体服务器发送 BYE 消息,断开消息8、9、12建立的同媒体服务器的Invite会话。
        final ClientTransaction clientTransaction = getMediaByeClientTransaction(ssrc);
        final Request bye = getByeRequest(clientTransaction);
        sendRequest(bye);
        FlowContextCache.setNewKey(ssrc, getCallId(bye));
        SSRCManger.releaseSSRC(ssrc);
    }

    private ClientTransaction getMediaByeClientTransaction(String ssrc) {
        final FlowContext context = FlowContextCache.get(ssrc);
        final ClientTransaction clientTransaction;
        if (Operation.PLAY == context.getOperation()) {
            clientTransaction = context.get(SIP_MEDIA_SESSION_2);
        } else {
            clientTransaction = context.get(PlaybackSession.SIP_MEDIA_SESSION_2);
        }
        return clientTransaction;
    }

    @Override
    public void playback(PlaybackParams playbackParams) {
        // 2:SIP服务器收到Invite请求后,通过三方呼叫控制建立媒体服务器和媒体流发送者之间的媒体连接。向媒体服务器发送Invite消息,此消息不携带SDP消息体。
        Request inviteMedia = getInviteRequest(
                createTo(properties.getMediaId(), properties.getMediaIp(), properties.getMediaPort()),
                createFrom(properties.getSipId(), properties.getSipIp(), properties.getSipPort()),
                playbackParams.getDeviceTransport());
        final ClientTransaction clientTransaction = sendRequest(inviteMedia);

        final FlowContext flowContext = new FlowContext(Operation.PLAYBACK, playbackParams);
        FlowContext.setProperties(properties);
        flowContext.put(PlaybackSession.SIP_MEDIA_SESSION_1, clientTransaction);
        FlowContextCache.put(getCallId(inviteMedia), flowContext);
    }

    @Override
    public void playbackForwardOrkBack(String ssrc, double scale) {
        final FlowContext flowContext = FlowContextCache.get(ssrc);
        final ClientTransaction deviceTransaction = flowContext.get(PlaybackSession.SIP_DEVICE_SESSION);
        // Scale为 1,正常播放;不等于 1,为正常播放速率的倍数;负数为倒放
        String content = "PLAY RTSP/1.0" + "\r\n"
                + "CSeq: 3" + "\r\n"
                + "Scale: " + scale;
        final Request infoRequest = SipRequestFactory.getInfoRequest(deviceTransaction, content.getBytes(StandardCharsets.UTF_8));
        RequestSender.sendRequest(infoRequest);
    }

    @Override
    public void playbackDrag(String ssrc) {
        final FlowContext flowContext = FlowContextCache.get(ssrc);
        final ClientTransaction deviceTransaction = flowContext.get(PlaybackSession.SIP_DEVICE_SESSION);
        String content = "PLAY RTSP/1.0" + "\r\n"
                + "CSeq:4" + "\r\n"
                + "Range: npt=100-";
        final Request infoRequest = SipRequestFactory.getInfoRequest(deviceTransaction, content.getBytes(StandardCharsets.UTF_8));
        RequestSender.sendRequest(infoRequest);
    }

    @Override
    public void playbackPause(String ssrc) {
        final FlowContext flowContext = FlowContextCache.get(ssrc);
        final ClientTransaction deviceTransaction = flowContext.get(PlaybackSession.SIP_DEVICE_SESSION);
        String content = "PAUSE RTSP/1.0" + "\r\n"
                + "CSeq: 1" + "\r\n"
                + "PauseTime: now";
        final Request infoRequest = SipRequestFactory.getInfoRequest(deviceTransaction, content.getBytes(StandardCharsets.UTF_8));
        RequestSender.sendRequest(infoRequest);
    }

    @Override
    public void playbackReplay(String ssrc) {
        final FlowContext flowContext = FlowContextCache.get(ssrc);
        final ClientTransaction deviceTransaction = flowContext.get(PlaybackSession.SIP_DEVICE_SESSION);
        String content = "PLAY RTSP/1.0" + "\r\n"
                + "CSeq: 2" + "\r\n"
                + "Range: npt=now-";
        final Request infoRequest = SipRequestFactory.getInfoRequest(deviceTransaction, content.getBytes(StandardCharsets.UTF_8));
        RequestSender.sendRequest(infoRequest);
    }
}
