package com.hlcy.yun.sip.gb28181.operation.flow.play;

import com.hlcy.yun.sip.gb28181.bean.Device;
import com.hlcy.yun.sip.gb28181.config.GB28181Properties;
import com.hlcy.yun.sip.gb28181.message.factory.SipRequestFactory;
import com.hlcy.yun.sip.gb28181.operation.ResponseProcessor;
import com.hlcy.yun.sip.gb28181.operation.flow.FlowContext;
import com.hlcy.yun.sip.gb28181.operation.flow.FlowContextCache;
import com.hlcy.yun.sip.gb28181.util.SSRCUtil;
import gov.nist.javax.sdp.fields.SessionNameField;

import javax.sdp.SdpException;
import javax.sdp.SessionDescription;
import javax.sip.ClientTransaction;
import javax.sip.ResponseEvent;
import javax.sip.message.Request;
import java.nio.charset.StandardCharsets;

import static com.hlcy.yun.sip.gb28181.client.RequestSender.sendRequest;
import static com.hlcy.yun.sip.gb28181.message.factory.SipRequestFactory.createFrom;
import static com.hlcy.yun.sip.gb28181.message.factory.SipRequestFactory.createTo;
import static com.hlcy.yun.sip.gb28181.operation.flow.play.PlaySession.SIP_DEVICE_SESSION;

/**
 * 客户端主动发起的实时视音频点播流程: 3->4<br/>
 * <p>
 * 3:媒体服务器收到SIP服务器的Invite请求后,回复200OK响应,携带SDP消息体,消息体中描述了媒体服务器接收媒体流的IP、端口、媒体格式等内容。<br/>
 * 4:SIP服务器收到媒体服务器返回的200OK响应后,向媒体流发送者发送Invite请求,请求中携带消息3中媒体服务器回复的200OK响应消息体,s字段为“Play”代表实时点播,增加y字段描述SSRC值,f字段描述媒体参数。<br/>
 * </p>
 */
public class MediaInviteResponseProcessor1 extends ResponseProcessor {

    /**
     * 流媒体服务器 200OK 响应的消息体
     * v=0
     * o=6401000000202000000100INIP4172.18.16.3
     * s=##ms20091214119
     * c=INIP4172.18.16.3
     * t=00
     * m=video6000RTP/AVP969897
     * a=recvonly
     * a=rtpmap:96PS/90000
     * <p>
     * 发送给设备(媒体流发送者)的 SDP 消息体
     * v=0
     * o=6401000000202000000100INIP4172.18.16.3
     * s=Play
     * c=INIP4172.18.16.3
     * t=00
     * m=video6000RTP/AVP969897
     * a=recvonly
     * a=rtpmap:96PS/90000
     * a=rtpmap:98H264/90000
     * a=rtpmap:97MPEG4/90000
     * y=0100000001
     */
    @Override
    protected void process(ResponseEvent event, FlowContext context) throws SdpException {
        SessionNameField sessionNameField = new SessionNameField();
        sessionNameField.setSessionName("Play");
        SessionDescription sessionDescription = getSessionDescription(getResponseBody1(event));
        sessionDescription.setSessionName(sessionNameField);

        Device device = context.getDevice();
        GB28181Properties properties = context.getProperties();
        final String ssrc = SSRCUtil.getPlaySsrc();

        Request inviteRequest2device = SipRequestFactory.getInviteRequest(
                createTo(device.getDeviceId(), device.getIp(), device.getPort()),
                createFrom(properties.getSipId(), properties.getSipIp(), properties.getSipPort()),
                device.getTransport(),
                (sessionDescription.toString() + "y=" + ssrc + "\r\n").getBytes(StandardCharsets.UTF_8));

        final ClientTransaction clientTransaction = sendRequest(inviteRequest2device);

        context.setSsrc(ssrc);
        context.put(SIP_DEVICE_SESSION, clientTransaction);

        FlowContextCache.setNewKey(getCallId(event), SipRequestFactory.getCallId(inviteRequest2device));
    }
}
