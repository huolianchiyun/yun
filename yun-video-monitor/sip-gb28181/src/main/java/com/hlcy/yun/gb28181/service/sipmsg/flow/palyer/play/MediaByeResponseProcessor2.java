package com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.play;

import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowResponseProcessor;
import com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;

import javax.sip.ClientTransaction;
import javax.sip.ResponseEvent;
import javax.sip.message.Request;

import static com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.play.PlaySession.SIP_DEVICE_SESSION;
import static com.hlcy.yun.gb28181.sip.client.RequestSender.sendByeRequest;

/**
 * 客户端主动发起的实时视音频点播流程: 18->19<br/>
 * <p>
 * 18:媒体服务器收到BYE消息后回复200OK响应,会话断开。<br/>
 * 19:SIP服务器向媒体流发送者发送BYE消息,断开消息4、5、7建立的同媒体流发送者的Invite会话。<br/>
 * </P>
 */
public class MediaByeResponseProcessor2 extends FlowResponseProcessor {
    @Override
    protected void process(ResponseEvent event, FlowContext context) {
        final ClientTransaction clientTransaction = context.get(SIP_DEVICE_SESSION);
        final Request bye = SipRequestFactory.getByeRequest(clientTransaction);
        sendByeRequest(bye, clientTransaction);
        FlowContextCacheUtil.setNewKey(getCallId(event), SipRequestFactory.getCallId(bye));
    }
}