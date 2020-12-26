package com.hlcy.yun.sip.gb28181.operation.flow.play;

import com.hlcy.yun.sip.gb28181.client.RequestSender;
import com.hlcy.yun.sip.gb28181.message.factory.SipRequestFactory;
import com.hlcy.yun.sip.gb28181.operation.ResponseProcessor;
import javax.sip.ResponseEvent;
import javax.sip.message.Request;

/**
 * 客户端主动发起的实时视音频点播流程: 3->4<br/>
 *<p>
 * 3:媒体服务器收到SIP服务器的Invite请求后,回复200OK响应,携带SDP消息体,消息体中描述了媒体服务器接收媒体流的IP、端口、媒体格式等内容。<br/>
 * 4:SIP服务器收到媒体服务器返回的200OK响应后,向媒体流发送者发送Invite请求,请求中携带消息3中媒体服务器回复的200OK响应消息体,s字段为“Play”代表实时点播,增加y字段描述SSRC值,f字段描述媒体参数。<br/>
 * </p>
 */
public class MediaInviteResponseProcessor1 extends ResponseProcessor {

    @Override
    public void process(ResponseEvent event) {


//
//        final Request inviteRequest = SipRequestFactory.getInviteRequest();
//        RequestSender.sendRequest(inviteRequest);
    }
}
