package com.hlcy.yun.gb28181.operation.flow.palyer.playback;

import com.hlcy.yun.gb28181.operation.ResponseProcessor;
import com.hlcy.yun.gb28181.operation.flow.FlowContext;
import com.hlcy.yun.gb28181.operation.flow.FlowContextCache;
import com.hlcy.yun.gb28181.sip.client.RequestSender;
import com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory;

import javax.sip.ClientTransaction;
import javax.sip.ResponseEvent;
import javax.sip.message.Request;

import static com.hlcy.yun.gb28181.operation.flow.palyer.play.PlaySession.SIP_DEVICE_SESSION;

/**
 * 客户端主动发起的历史视音频回放流程: 18->19<br/>
 * <P>
 * 18:媒体服务器收到BYE消息后回复200OK响应,会话断开。<br/>
 * 19:SIP服务器向媒体流发送者发送BYE消息,断开消息4、5、7建立的同媒体流发送者的Invite会话。<br/>
 * </P>
 */
public class MediaByeResponseProcessor2 extends ResponseProcessor {
    @Override
    protected void process(ResponseEvent event, FlowContext context) {
        final ClientTransaction clientTransaction = context.get(SIP_DEVICE_SESSION);
        final Request bye = SipRequestFactory.getByeRequest(clientTransaction);
        RequestSender.sendRequest(bye);

        FlowContextCache.setNewKey(getCallId(event), SipRequestFactory.getCallId(bye));
    }
}
