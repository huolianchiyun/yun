package com.hlcy.yun.sip.gb28181.operation.flow.play;

import com.hlcy.yun.sip.gb28181.operation.ResponseProcessor;
import com.hlcy.yun.sip.gb28181.operation.flow.FlowContext;

import javax.sip.ResponseEvent;

/**
 * 客户端主动发起的实时视音频点播流程: 5->6->7->8->9<br/>
 *<P>
 * 5:媒体流发送者收到SIP服务器的Invite请求后,回复200OK响应,携带SDP消息体,消息体中描述了媒体流发送者发送媒体流的IP、端口、媒体格式、SSRC字段等内容。<br/>
 * 6:SIP服务器收到媒体流发送者返回的200OK响应后,向媒体服务器发送ACK请求,请求中携带消息5中媒体流发送者回复的200OK响应消息体,完成与媒体服务器的Invite会话建立过程。<br/>
 * 7:SIP服务器收到媒体流发送者返回的200OK响应后,向媒体流发送者发送ACK请求,请求中不携带消息体,完成与媒体流发送者的Invite会话建立过程。<br/>
 * 8:完成三方呼叫控制后,SIP服务器通过B2BUA代理方式建立媒体流接收者和媒体服务器之间的媒体连接。在消息1中增加SSRC值,转发给媒体服务器。<br/>
 * </P>
 */
public class DeviceInviteResponseProcessor extends ResponseProcessor {
    @Override
    protected void process(ResponseEvent event, FlowContext context) {

    }
}
