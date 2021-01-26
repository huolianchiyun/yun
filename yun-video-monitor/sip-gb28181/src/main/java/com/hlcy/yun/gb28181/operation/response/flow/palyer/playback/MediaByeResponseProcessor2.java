package com.hlcy.yun.gb28181.operation.response.flow.palyer.playback;

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
 * 客户端主动发起的历史视音频回放流程: 18->19<br/>
 * <p>
 * 18:媒体服务器收到BYE消息后回复200OK响应,会话断开。<br/>
 * 19:SIP服务器向媒体流发送者发送BYE消息,断开消息4、5、7建立的同媒体流发送者的Invite会话。<br/>
 * </P>
 */
public class MediaByeResponseProcessor2 extends FlowResponseProcessor {
    @Override
    protected void process(ResponseEvent event, FlowContext context) {
        final ClientTransaction clientTransaction = context.get(PlaySession.SIP_DEVICE_SESSION);
        final Request bye = SipRequestFactory.getByeRequest(clientTransaction);
        RequestSender.sendByeRequest(bye, clientTransaction);

        FlowContextCacheUtil.setNewKey(getCallId(event), SipRequestFactory.getCallId(bye));
    }
}
