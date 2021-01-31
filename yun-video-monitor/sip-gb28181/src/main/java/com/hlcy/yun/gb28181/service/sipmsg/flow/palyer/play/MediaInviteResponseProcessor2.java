package com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.play;

import com.hlcy.yun.gb28181.bean.PlayResponse;
import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowResponseProcessor;
import com.hlcy.yun.gb28181.sip.biz.RequestSender;
import com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory;

import javax.sip.ClientTransaction;
import javax.sip.ResponseEvent;
import javax.sip.message.Request;


/**
 * 客户端主动发起的实时视音频点播流程: 9->10->11->12<br/>
 * <p>
 * 9:媒体服务器收到Invite请求,回复 200OK 响应,携带SDP消息体,消息体中描述了媒体服务器发送媒体流的IP、端口、媒体格式、SSRC值等内容。<br/>
 * 10:SIP服务器将消息9转发给媒体流接收者。<br/>
 * 11:媒体流接收者收到200OK响应后,回复ACK消息,完成与SIP服务器的Invite会话建立过程。<br/>
 * 12:SIP服务器将消息11转发给媒体服务器,完成与媒体服务器的Invite会话建立过程。<br/>
 * </p>
 */
public class MediaInviteResponseProcessor2 extends FlowResponseProcessor {
    @Override
    protected void process(ResponseEvent event, FlowContext context) {
        final String ssrc = context.getSsrc();
        DeferredResultHolder.setDeferredResultForRequest(
                DeferredResultHolder.CALLBACK_CMD_PLAY + context.getOperationalParams().getChannelId(),
                new PlayResponse(ssrc, context.getProperties().getMediaIp()));

        final ClientTransaction mediaTransaction = context.getClientTransaction(PlaySession.SIP_MEDIA_SESSION_2);
        final Request ackRequest4Media = SipRequestFactory.getAckRequest(mediaTransaction, getMessageBodyByByteArr(event.getResponse()));
        RequestSender.sendAckRequest(ackRequest4Media, mediaTransaction);
        FlowContextCacheUtil.setNewKey(getCallId(event.getResponse()), ssrc);
    }
}
