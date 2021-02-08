package com.hlcy.yun.gb28181.service;

import com.hlcy.yun.common.utils.http.RestHttpClient;
import com.hlcy.yun.gb28181.bean.PlayResponse;
import com.hlcy.yun.gb28181.service.params.player.PlayParams;
import com.hlcy.yun.gb28181.service.params.player.PlaybackParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.sipmsg.flow.Operation;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.playback.PlaybackSession;
import com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory;
import com.hlcy.yun.gb28181.util.SSRCManger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.sip.ClientTransaction;
import javax.sip.message.Request;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

import static com.hlcy.yun.gb28181.sip.biz.RequestSender.sendByeRequest;
import static com.hlcy.yun.gb28181.sip.biz.RequestSender.sendRequest;
import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.*;
import static com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.play.PlaySession.SIP_MEDIA_SESSION_1;
import static com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.play.PlaySession.SIP_MEDIA_SESSION_2;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultPlayer implements Player {
    private final GB28181Properties properties;

    @Override
    public void play(PlayParams params) {
        if (!params.isNewStream()) {
            // 检验该设备是否已经点播，若已点播，则返回已点播的 SSRC
            final Optional<FlowContext> optional = FlowContextCacheUtil.findFlowContextBy(params.getChannelId());
            if (optional.isPresent() && StringUtils.hasText(optional.get().getSsrc())) {
                DeferredResultHolder.setDeferredResultForRequest(
                        DeferredResultHolder.CALLBACK_CMD_PLAY + params.getChannelId(),
                        new PlayResponse(optional.get().getSsrc(), properties.getMediaIp()));
                return;
            }
        }

        if (!handleMediaPullStream(params)) {
            // 2:向媒体服务器发送Invite消息,此消息不携带SDP消息体。
            Request inviteMedia = getInviteRequest(
                    createTo(properties.getMediaId(), properties.getMediaIp(), properties.getMediaVideoPort()),
                    createFrom(properties.getSipId(), properties.getSipIp(), properties.getSipPort()),
                    params.getDeviceTransport());
            final ClientTransaction clientTransaction = sendRequest(inviteMedia);

            final FlowContext flowContext = new FlowContext(Operation.PLAY, params);
            FlowContext.setProperties(properties);
            flowContext.put(SIP_MEDIA_SESSION_1, clientTransaction);
            FlowContextCacheUtil.put(getCallId(inviteMedia), flowContext);
        }
    }

    private boolean handleMediaPullStream(PlayParams params) {
        if (!StringUtils.isEmpty(params.getFormat())) {
            final String ssrc = SSRCManger.getPlaySSRC();
            // 是否让流媒体主动拉流
            final String url = properties.getMediaPullStreamApi()
                    .replace("${mediaIp}", properties.getMediaIp())
                    .replace("${ssrc}", ssrc)
                    .replace("${deviceIp}", params.getDeviceIp());
            final Map response = RestHttpClient.exchange(url, HttpMethod.GET, new ParameterizedTypeReference<Map<String, Object>>() {
            }, null);
            if (Integer.parseInt(response.get("code").toString()) == 0) {
                FlowContextCacheUtil.put(ssrc, new FlowContext(Operation.PLAY, params, true));
                DeferredResultHolder.setDeferredResultForRequest(
                        DeferredResultHolder.CALLBACK_CMD_PLAY + params.getChannelId(),
                        new PlayResponse(ssrc, properties.getMediaIp()));
            } else {
                log.error("*** 流媒体主动从设备拉流失败， response：{}", response);
                DeferredResultHolder.setErrorDeferredResultForRequest(
                        DeferredResultHolder.CALLBACK_CMD_PLAY + params.getChannelId(),
                        String.format("*** 流媒体主动从设备拉流失败， response：%s", response));
            }
            return true;
        }
        return false;
    }

    @Override
    public void stop(String ssrc) {
        // 点播 15:SIP服务器收到BYE消息后向媒体服务器发送BYE消息,断开消息8、9、12建立的同媒体服务器的Invite会话。
        // 回放 23:SIP服务器收到 BYE 消息后向媒体服务器发送 BYE 消息,断开消息8、9、12建立的同媒体服务器的Invite会话。
        if (isClosedMediaStreamOf(ssrc)) {
            return;
        }

        if (!handleMediaPullStreamClose(ssrc)) {
            final ClientTransaction clientTransaction = getMediaByeClientTransaction(ssrc);
            final Request bye = getByeRequest(clientTransaction);
            sendByeRequest(bye, clientTransaction);

            FlowContextCacheUtil.setNewKey(ssrc, getCallId(bye));
            SSRCManger.releaseSSRC(ssrc);
        }
    }

    private boolean isClosedMediaStreamOf(String ssrc) {
        final FlowContext context = FlowContextCacheUtil.get(ssrc);
        if (context == null) {
            log.info("视频流已关闭，SSRC：{}", ssrc);
            return true;
        }
        if (context.isCleanup() > 1) {
            log.info("视频流正在关闭中... ...，SSRC：{}", ssrc);
            return true;
        }
        return false;
    }

    private boolean handleMediaPullStreamClose(String ssrc) {
        final FlowContext context = FlowContextCacheUtil.get(ssrc);
        if (context.isMediaPullStream()) {
            log.info("*** Media pull stream close, ssrc:{} ***", ssrc);
            SSRCManger.releaseSSRC(ssrc);
            return true;
        }
        return false;
    }

    private ClientTransaction getMediaByeClientTransaction(String ssrc) {
        final FlowContext context = FlowContextCacheUtil.get(ssrc);
        ClientTransaction clientTransaction = null;
        if (Operation.PLAY == context.getOperation()) {
            clientTransaction = context.getClientTransaction(SIP_MEDIA_SESSION_2);
        } else if (Operation.PLAYBACK == context.getOperation()) {
            clientTransaction = context.getClientTransaction(PlaybackSession.SIP_MEDIA_SESSION_2);
        }
        Assert.notNull(clientTransaction, String.format("The clientTransaction of SIP_MEDIA_SESSION_2 has been lost, SSRC: %s", ssrc));
        return clientTransaction;
    }

    @Override
    public void playback(PlaybackParams playbackParams) {
        // 2:SIP服务器收到Invite请求后,通过三方呼叫控制建立媒体服务器和媒体流发送者之间的媒体连接。向媒体服务器发送Invite消息,此消息不携带SDP消息体。
        Request inviteMedia = getInviteRequest(
                createTo(properties.getMediaId(), properties.getMediaIp(), properties.getMediaVideoPort()),
                createFrom(properties.getSipId(), properties.getSipIp(), properties.getSipPort()),
                playbackParams.getDeviceTransport());
        final ClientTransaction clientTransaction = sendRequest(inviteMedia);

        final FlowContext flowContext = new FlowContext(Operation.PLAYBACK, playbackParams);
        FlowContext.setProperties(properties);
        flowContext.put(PlaybackSession.SIP_MEDIA_SESSION_1, clientTransaction);
        FlowContextCacheUtil.put(getCallId(inviteMedia), flowContext);
    }

    @Override
    public void playbackForwardOrkBack(String ssrc, double scale) {
        final FlowContext flowContext = FlowContextCacheUtil.get(ssrc);
        final ClientTransaction deviceTransaction = flowContext.getClientTransaction(PlaybackSession.SIP_DEVICE_SESSION);
        // Scale为 1,正常播放;不等于 1,为正常播放速率的倍数;负数为倒放
        String content = "PLAY RTSP/1.0" + "\r\n"
                + "CSeq: 3" + "\r\n"
                + "Scale: " + scale;
        final Request infoRequest = SipRequestFactory.getInfoRequest(deviceTransaction, content.getBytes(StandardCharsets.UTF_8));
        flowContext.put(PlaybackSession.SIP_DEVICE_SESSION, sendRequest(infoRequest));
    }

    @Override
    public void playbackDrag(String ssrc, int range) {
        final FlowContext flowContext = FlowContextCacheUtil.get(ssrc);
        final ClientTransaction deviceTransaction = flowContext.getClientTransaction(PlaybackSession.SIP_DEVICE_SESSION);
        String content = "PLAY RTSP/1.0" + "\r\n"
                + "CSeq:4" + "\r\n"
                + "Range: npt="
                + range
                + "-";
        final Request infoRequest = SipRequestFactory.getInfoRequest(deviceTransaction, content.getBytes(StandardCharsets.UTF_8));
        flowContext.put(PlaybackSession.SIP_DEVICE_SESSION, sendRequest(infoRequest));
    }

    @Override
    public void playbackPause(String ssrc) {
        final FlowContext flowContext = FlowContextCacheUtil.get(ssrc);
        ClientTransaction deviceTransaction = flowContext.getClientTransaction(PlaybackSession.SIP_DEVICE_SESSION);
        String content = "PAUSE RTSP/1.0" + "\r\n"
                + "CSeq: 1" + "\r\n"
                + "PauseTime: now";
        final Request infoRequest = SipRequestFactory.getInfoRequest(deviceTransaction, content.getBytes(StandardCharsets.UTF_8));
        flowContext.put(PlaybackSession.SIP_DEVICE_SESSION, sendRequest(infoRequest));
    }

    @Override
    public void playbackReplay(String ssrc) {
        final FlowContext flowContext = FlowContextCacheUtil.get(ssrc);
        final ClientTransaction deviceTransaction = flowContext.getClientTransaction(PlaybackSession.SIP_DEVICE_SESSION);
        String content = "PLAY RTSP/1.0" + "\r\n"
                + "CSeq: 2" + "\r\n"
                + "Range: npt=now-";
        final Request infoRequest = SipRequestFactory.getInfoRequest(deviceTransaction, content.getBytes(StandardCharsets.UTF_8));
        flowContext.put(PlaybackSession.SIP_DEVICE_SESSION, sendRequest(infoRequest));
    }
}
