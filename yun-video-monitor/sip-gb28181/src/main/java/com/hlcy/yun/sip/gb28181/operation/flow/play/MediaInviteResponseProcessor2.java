package com.hlcy.yun.sip.gb28181.operation.flow.play;

import com.hlcy.yun.sip.gb28181.operation.ResponseProcessor;

import javax.sip.ResponseEvent;


/**
 * 客户端主动发起的实时视音频点播流程: 9->10->11->12<br/>
 * <p>
 * 9:媒体服务器收到Invite请求,回复200OK响应,携带SDP消息体,消息体中描述了媒体服务器发送媒体流的IP、端口、媒体格式、SSRC值等内容。<br/>
 * 10:SIP服务器将消息9转发给媒体流接收者。<br/>
 * 11:媒体流接收者收到200OK响应后,回复ACK消息,完成与SIP服务器的Invite会话建立过程。<br/>
 * 12:SIP服务器将消息11转发给媒体服务器,完成与媒体服务器的Invite会话建立过程。<br/>
 * </p>
 */
public class MediaInviteResponseProcessor2 extends ResponseProcessor {
    @Override
    public void process(ResponseEvent event) {

    }
}
