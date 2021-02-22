package com.hlcy.yun.gb28181.service;

import com.alibaba.fastjson.JSONObject;
import com.hlcy.yun.gb28181.client.MediaClient;
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
import com.hlcy.yun.gb28181.ssrc.SSRCManger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.sip.ClientTransaction;
import javax.sip.message.Request;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static com.hlcy.yun.gb28181.service.sipmsg.flow.FlowPipelineFactory.getResponseFlowPipeline;
import static com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.play.PlaySession.*;
import static com.hlcy.yun.gb28181.sip.biz.RequestSender.sendByeRequest;
import static com.hlcy.yun.gb28181.sip.biz.RequestSender.sendRequest;
import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultPlayer implements Player {
    private final GB28181Properties properties;

    private final MediaClient mediaClient;

    @Override
    public void play(PlayParams params) {
        if (!params.isNewStream()) {
            // 检验该设备是否已经点播，若已点播，则返回已点播的 SSRC
            final Optional<FlowContext> optional = FlowContextCacheUtil.findFlowContextByOperationAndChannelId(Operation.PLAY, params.getChannelId());
            if (optional.isPresent() && StringUtils.hasText(optional.get().getSsrc())) {
                final FlowContext flowContext = optional.get();
                if (!flowContext.expire()) {
                    if (mediaClient.isValidTestSsrc(flowContext.getSsrc())) {
                        log.info("*** 设备:{}, 已经推流，返回之前的SSRC：{}", flowContext.getOperationalParams().getChannelId(), flowContext.getSsrc());
                        DeferredResultHolder.setDeferredResultForRequest(
                                params.getCallbackKey(),
                                new PlayResponse(optional.get().getSsrc(), properties.getMediaIp()));
                        return;
                    } else {
                        stop(flowContext.getSsrc());
                    }
                }
            }
        }

        if (!handleMediaMakeDevicePushStream(params)) {
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

    private boolean handleMediaMakeDevicePushStream(PlayParams params) {
        if (!StringUtils.isEmpty(params.getFormat())) {
            final String ssrc = SSRCManger.getPlaySSRC();
            // 是否让流媒体主动拉流
            final JSONObject response = mediaClient.mediaMakeDevicePushStream(ssrc, params.getDeviceIp());
            if (response.getIntValue("code") == 0) {
                FlowContextCacheUtil.put(ssrc, new FlowContext(Operation.PLAY, params, true));
                DeferredResultHolder.setDeferredResultForRequest(
                        params.getCallbackKey(),
                        new PlayResponse(ssrc, properties.getMediaIp()));
            } else {
                log.error("*** 流媒体使设备推流失败， response：{}", response);
                DeferredResultHolder.setErrorDeferredResultForRequest(
                        params.getCallbackKey(),
                        String.format("*** 流媒体使设备推流失败， response：%s", response));
            }
            return true;
        }
        return false;
    }

    @Override
    public void stop(String ssrc) {
        if (isClosedMediaStreamOf(ssrc)) {
            return;
        }

        if (!handleMediaMakeDevicePushStreamClose(ssrc)) {
            final ClientTransaction clientTransaction = getMediaByeClientTransaction(ssrc);
            if (clientTransaction != null) {
                final Request bye = getByeRequest(clientTransaction);
                sendByeRequest(bye, clientTransaction);
                FlowContextCacheUtil.setNewKey(ssrc, getCallId(bye));
            }
        }
    }

    private boolean isClosedMediaStreamOf(String ssrc) {
        final Optional<FlowContext> context = FlowContextCacheUtil.findFlowContextBySsrc(ssrc);
        if (!context.isPresent()) {
            log.info("视频流已关闭，SSRC：{}", ssrc);
            return true;
        }
        if (context.get().isCleanup()) {
            log.info("视频流正在关闭中... ...，SSRC：{}", ssrc);
            return true;
        }
        return false;
    }

    private boolean handleMediaMakeDevicePushStreamClose(String ssrc) {
        final Optional<FlowContext> contextOptional = FlowContextCacheUtil.findFlowContextBySsrc(ssrc);
        if (contextOptional.isPresent() && contextOptional.get().isMediaPullStream()) {
            log.info("*** Media make device push stream close, ssrc:{} ***", ssrc);
            SSRCManger.releaseSSRC(ssrc);
            return true;
        }
        return false;
    }

    private ClientTransaction getMediaByeClientTransaction(String ssrc) {
        ClientTransaction clientTransaction = null;
        final Optional<FlowContext> optional = FlowContextCacheUtil.findFlowContextBySsrc(ssrc);
        if (optional.isPresent()) {
            final FlowContext context = optional.get();
            if (Operation.PLAY == context.getOperation()) {
                clientTransaction = context.isFromDeserialization()
                        ? context.getClientTransaction(SIP_DEVICE_SESSION)
                        : context.getClientTransaction(SIP_MEDIA_SESSION_2);
            } else if (Operation.PLAYBACK == context.getOperation()) {
                clientTransaction = context.isFromDeserialization()
                        ? context.getClientTransaction(PlaybackSession.SIP_DEVICE_SESSION)
                        : context.getClientTransaction(PlaybackSession.SIP_MEDIA_SESSION_2);
            }
            if (clientTransaction == null) {
                log.warn("*** The clientTransaction of {} has been lost, SSRC: {}", context.getOperation(), ssrc);
            }
            if (context.isFromDeserialization()) {
                // 将当前响应处理器切换为 device by response
                context.setCurrentResponseProcessor(getResponseFlowPipeline(context.getOperation()).get("DeviceByResponseProcessor"));
            }
        } else {
            log.warn("*** The FlowContext has been lost when get clientTransaction, SSRC: {}", ssrc);
        }
        return clientTransaction;
    }

    @Override
    public void playback(PlaybackParams playbackParams) {
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
        String content = "PLAY RTSP/1.0" + "\r\n"
                + "CSeq: 3" + "\r\n"
                + "Scale: " + scale + "\r\n";
        final Request infoRequest = SipRequestFactory.getInfoRequest(deviceTransaction, content.getBytes(StandardCharsets.UTF_8));
        flowContext.put(PlaybackSession.SIP_DEVICE_SESSION, sendRequest(infoRequest));
    }

    @Override
    public void playbackDrag(String ssrc, int range) {
        final FlowContext flowContext = FlowContextCacheUtil.get(ssrc);
        final ClientTransaction deviceTransaction = flowContext.getClientTransaction(PlaybackSession.SIP_DEVICE_SESSION);
        String content = "PLAY RTSP/1.0" + "\r\n"
                + "CSeq: 4" + "\r\n"
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
