package com.hlcy.yun.gb28181.operation.response.flow.palyer.playback;

import com.hlcy.yun.gb28181.operation.response.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.operation.response.flow.FlowResponseProcessor;
import com.hlcy.yun.gb28181.operation.response.flow.palyer.play.PlaySession;
import com.hlcy.yun.gb28181.operation.response.flow.FlowContext;
import com.hlcy.yun.gb28181.operation.response.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.sip.client.RequestSender;
import com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory;

import javax.sip.ClientTransaction;
import javax.sip.ResponseEvent;
import javax.sip.message.Request;


/**
 * 客户端主动发起的历史视音频回放流程: 9->10->11->12->13->14<br/>
 * <p>
 * 9:媒体服务器收到Invite请求,回复200OK响应,携带SDP消息体,消息体中描述了媒体服务器发送媒体流的IP、端口、媒体格式、SSRC值等内容。<br/>
 * 10:SIP服务器将消息9转发给媒体流接收者。<br/>
 * 11:媒体流接收者收到200OK响应后,回复ACK消息,完成与SIP服务器的Invite会话建立过程。<br/>
 * 12:SIP服务器将消息11转发给媒体服务器,完成与媒体服务器的Invite会话建立过程。<br/>
 * 13:在回放过程中,媒体流接收者通过向SIP服务器发送会话内Info消息进行回放控制,包括视频的暂停、播放、快放、慢放、随机拖放播放等操作,Info消息体见附录B。<br/>
 * 14:媒体流发送者收到Info消息后回复200OK响应;<br/>
 * </p>
 */
public class MediaInviteResponseProcessor2 extends FlowResponseProcessor {
    @Override
    protected void process(ResponseEvent event, FlowContext context) {
        final String ssrc = context.getSsrc();
        DeferredResultHolder.setDeferredResultForRequest(DeferredResultHolder.CALLBACK_CMD_PLAYBACK + context.getOperationalParams().getChannelId(), ssrc);

        final ClientTransaction mediaTransaction = context.get(PlaySession.SIP_MEDIA_SESSION_2);
        final Request ackRequest4Media = SipRequestFactory.getAckRequest(mediaTransaction, getResponseBodyByByteArr(event));
        RequestSender.sendAckRequest(ackRequest4Media, mediaTransaction);
        FlowContextCacheUtil.setNewKey(getCallId(event), ssrc);
    }
}
