package com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.playback;

import com.hlcy.yun.gb28181.service.params.PlaybackParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.exception.SSRCException;
import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowResponseProcessor;
import com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.play.PlaySession;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.sip.client.RequestSender;
import com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory;
import com.hlcy.yun.gb28181.util.SSRCManger;
import gov.nist.javax.sdp.fields.SSRCField;
import gov.nist.javax.sdp.fields.SessionNameField;
import gov.nist.javax.sdp.fields.URIField;
import lombok.extern.slf4j.Slf4j;

import javax.sdp.SdpException;
import javax.sdp.SessionDescription;
import javax.sip.ClientTransaction;
import javax.sip.ResponseEvent;
import javax.sip.message.Request;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Vector;

/**
 * 客户端主动发起的历史视音频回放流程: 3->4<br/>
 * <p>
 * 3:媒体服务器收到SIP服务器的Invite请求后,回复200OK响应,携带SDP消息体,消息体中描述了媒体服务器接收媒体流的IP、端口、媒体格式等内容。<br/>
 * 4:SIP服务器收到媒体服务器返回的200OK响应后,向媒体流发送者发送Invite请求,请求中携带消息3中媒体服务器回复的200OK响应消息体,s字段为“Playback”代表历史回放,u字段代表回放通道ID和回放类型,t字段代表回放时间段,增加y字段描述SSRC值,f字段描述媒体参数。<br/>
 * </p>
 */
@Slf4j
public class MediaInviteResponseProcessor1 extends FlowResponseProcessor {

    /**
     * <p>
     * SIP/2.0 200 OK
     * Via:SIP/2.0/UDP 源域名或IP地址
     * From:<sip:SIP服务器编码@源域名>;tag=1ad9931d
     * To:<sip:媒体服务器编码@目的域名>;tag=3094947605
     * Call-ID:wlss-11df50d7-730beb6350a5506aa8316d9dc100cf6b@172.18.16.5
     * CSeq:1 Invite
     * Contact:<sip:媒体服务器编码@目的域名或IP地址端口>
     * Content-Type:APPLICATION/SDPContent-Length:消息实体的字节长度
     * <p>
     * v=0
     * o=64010000002020000001 0 0 IN IP4 172.18.16.3
     * s=# # ms20091214
     * c=IN IP4 172.18.16.3
     * t=0 0
     * m=video 6000 RTP/AVP 96 98 97
     * a=recvonly
     * a=rtpmap:96 PS/90000
     * a=rtpmap:98 H264/90000
     * a=rtpmap:97 MPEG4/90000
     * </p>
     * <p>
     * 发送给设备(媒体流发送者)的 SDP 消息体
     * Invite sip:媒体流发送者设备编码@目的域名或IP地址端口 SIP/2.0
     * To:sip:媒体流发送者设备编码@目的域名
     * Content-Length:消息实体的字节长度
     * Contact:<sip:SIP服务器编码@源IP地址端口>
     * CSeq:1 Invite
     * Call-ID:wlss-e680b2c1-730beb6350a5506aa8316d9dc100cf6b@172.18.16.5
     * Via:SIP/2.0/UDP 源域名或IP地址
     * From:<sip:SIP服务器编码@源域名>;tag=f569d024
     * Content-Type:APPLICATION/SDP
     * Subject:媒体流发送者设备编码:发送端媒体流序列号,媒体流接收者设备编码:接收端媒体流序列号
     * Max-Forwards:70
     * <p>
     * v=0
     * o=64010000002020000001 0 0 IN IP4 172.18.16.3
     * s=Playback
     * u=64010000041310000345:3
     * c=IN IP4 172.18.16.3
     * t=1288625085 1288625871
     * m=video 6000 RTP/AVP 96 98 97
     * a=recvonly
     * a=rtpmap:96 PS/90000
     * a=rtpmap:98 H264/90000
     * a=rtpmap:97 MPEG4/90000
     * y=0100000001
     * </p>
     */
    @Override
    protected void process(ResponseEvent event, FlowContext context) throws SdpException {
        SessionNameField sessionNameField = new SessionNameField("Playback");
        SessionDescription sessionDescription = getSessionDescription(getResponseBodyByStr(event));
        sessionDescription.setSessionName(sessionNameField);

        final PlaybackParams playbackParams = (PlaybackParams) context.getOperationalParams();
        sessionDescription.setTimeDescriptions(new Vector<>(Arrays.asList(playbackParams.getStartTimestamp(), playbackParams.getEndTimestamp())));

        final String ssrc = getSSRC(context);
        sessionDescription.setSSRC(new SSRCField(ssrc));
        final URIField uriField = new URIField();
        uriField.setURI(playbackParams.getChannelId() + ":" + playbackParams.getPlaybackType());
        sessionDescription.setURI(uriField);

        GB28181Properties properties = context.getProperties();
        Request inviteRequest2device = SipRequestFactory.getInviteRequest(
                SipRequestFactory.createTo(playbackParams.getChannelId(), playbackParams.getDeviceIp(), playbackParams.getDevicePort()),
                SipRequestFactory.createFrom(properties.getSipId(), properties.getSipIp(), properties.getSipPort()),
                playbackParams.getDeviceTransport(),
                sessionDescription.toString().getBytes(StandardCharsets.UTF_8));

        final ClientTransaction clientTransaction = RequestSender.sendRequest(inviteRequest2device);

        context.setSsrc(ssrc);
        context.put(PlaySession.SIP_DEVICE_SESSION, clientTransaction);
        FlowContextCacheUtil.setNewKey(getCallId(event), SipRequestFactory.getCallId(inviteRequest2device));
    }

    private String getSSRC(FlowContext context) {
        try {
            return SSRCManger.getPlaySSRC();
        } catch (SSRCException e) {
            log.warn(e.getMessage());
            DeferredResultHolder.setErrorDeferredResultForRequest(
                    DeferredResultHolder.CALLBACK_CMD_PLAYBACK + context.getOperationalParams().getChannelId(), e.getMessage());
            throw e;
        }
    }
}
