package com.hlcy.yun.gb28181.operation.flow.palyer.play;

import com.hlcy.yun.gb28181.sip.client.RequestSender;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory;
import com.hlcy.yun.gb28181.operation.flow.FlowContext;
import com.hlcy.yun.gb28181.operation.flow.FlowContextCache;
import com.hlcy.yun.gb28181.operation.ResponseProcessor;

import javax.sip.ClientTransaction;
import javax.sip.ResponseEvent;
import javax.sip.message.Request;

import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.createFrom;
import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.createTo;

/**
 * 客户端主动发起的实时视音频点播流程: 5->6->7->8->9<br/>
 * <p>
 * 5:媒体流发送者收到SIP服务器的Invite请求后,回复200OK响应,携带SDP消息体,消息体中描述了媒体流发送者发送媒体流的IP、端口、媒体格式、SSRC字段等内容。<br/>
 * 6:SIP服务器收到媒体流发送者返回的200OK响应后,向媒体服务器发送ACK请求,请求中携带消息5中媒体流发送者回复的200OK响应消息体,完成与媒体服务器的Invite会话建立过程。<br/>
 * 7:SIP服务器收到媒体流发送者返回的200OK响应后,向媒体流发送者发送ACK请求,请求中不携带消息体,完成与媒体流发送者的Invite会话建立过程。<br/>
 * 8:完成三方呼叫控制后,SIP服务器通过B2BUA代理方式建立媒体流接收者和媒体服务器之间的媒体连接。在消息1中增加SSRC值,转发给媒体服务器。<br/>
 * </P>
 */
public class DeviceInviteResponseProcessor extends ResponseProcessor {
    @Override
    protected void process(ResponseEvent event, FlowContext context) {
        final ClientTransaction mediaTransaction = context.get(PlaySession.SIP_MEDIA_SESSION_1);
        final Request ackRequest4Media = SipRequestFactory.getAckRequest(mediaTransaction, getResponseBody2(event));
        RequestSender.sendAckRequest(ackRequest4Media, mediaTransaction);

        final ClientTransaction deviceTransaction = context.get(PlaySession.SIP_DEVICE_SESSION);
        final Request ackRequest4Device = SipRequestFactory.getAckRequest(deviceTransaction);
        RequestSender.sendAckRequest(ackRequest4Device, deviceTransaction);

        final Request inviteRequest4Device = deviceTransaction.getRequest();
        final GB28181Properties properties = context.getProperties();
        Request inviteRequest2media = SipRequestFactory.getInviteRequest(
                SipRequestFactory.createTo(properties.getMediaId(), properties.getMediaIp(), properties.getMediaPort()),
                SipRequestFactory.createFrom(properties.getSipId(), properties.getSipIp(), properties.getSipPort()),
                context.getPlayParams().getDeviceTransport(),
                inviteRequest4Device.getRawContent());

        final ClientTransaction clientTransaction = RequestSender.sendRequest(inviteRequest2media);

        context.put(PlaySession.SIP_MEDIA_SESSION_2, clientTransaction);
        FlowContextCache.setNewKey(getCallId(event), SipRequestFactory.getCallId(inviteRequest2media));
    }
}
