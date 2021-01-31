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
import com.hlcy.yun.gb28181.util.SSRCManger;
import gov.nist.javax.sdp.fields.SSRCField;
import gov.nist.javax.sdp.fields.SessionNameField;
import lombok.extern.slf4j.Slf4j;

import javax.sdp.SdpException;
import javax.sdp.SessionDescription;
import javax.sip.ClientTransaction;
import javax.sip.ResponseEvent;
import javax.sip.message.Request;
import java.nio.charset.StandardCharsets;

/**
 * 客户端主动发起的实时视音频点播流程: 3->4<br/>
 * <p>
 * 3:媒体服务器收到SIP服务器的Invite请求后,回复200OK响应,携带SDP消息体,消息体中描述了媒体服务器接收媒体流的IP、端口、媒体格式等内容。<br/>
 * 4:SIP服务器收到媒体服务器返回的200OK响应后,向媒体流发送者发送Invite请求,请求中携带消息3中媒体服务器回复的200OK响应消息体,s字段为“Play”代表实时点播,增加y字段描述SSRC值,f字段描述媒体参数。<br/>
 * </p>
 */
@Slf4j
public class MediaInviteResponseProcessor1 extends FlowResponseProcessor {

    /**
     * 流媒体服务器 200OK 响应的消息体
     * v=0
     * o=64010000002020000001 0 0 IN IP4 172.18.16.3
     * s=##ms20091214119
     * c=IN IP4 172.18.16.3
     * t=0 0
     * m=video 6000 RTP/AVP 96 98 97
     * a=recvonly
     * a=rtpmap:96 PS/90000
     * <p>
     * 发送给设备(媒体流发送者)的 SDP 消息体
     * v=0
     * o=64010000002020000001 0 0 IN IP4 172.18.16.3
     * s=Play
     * c=IN IP4 172.18.16.3
     * t=00
     * m=video 6000 RTP/AVP 96 98 97
     * a=recvonly
     * a=rtpmap:96 PS/90000
     * a=rtpmap:98 H264/90000
     * a=rtpmap:97 MPEG4/90000
     * y=0100000001
     */
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
            DeferredResultHolder.setErrorDeferredResultForRequest(
                    DeferredResultHolder.CALLBACK_CMD_PLAY + context.getOperationalParams().getChannelId(), e.getMessage());
            throw e;
        }
    }
}
