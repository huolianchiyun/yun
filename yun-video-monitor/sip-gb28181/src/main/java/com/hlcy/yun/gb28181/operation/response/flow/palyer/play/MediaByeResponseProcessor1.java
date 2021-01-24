package com.hlcy.yun.gb28181.operation.response.flow.palyer.play;

import com.hlcy.yun.gb28181.operation.response.flow.FlowContext;
import com.hlcy.yun.gb28181.operation.response.flow.FlowContextCache;
import com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory;
import com.hlcy.yun.gb28181.operation.response.flow.ResponseProcessor;

import javax.sip.ClientTransaction;
import javax.sip.ResponseEvent;
import javax.sip.message.Request;

import static com.hlcy.yun.gb28181.sip.client.RequestSender.sendByeRequest;

/**
 * 客户端主动发起的实时视音频点播流程: 16->17<br/>
 * <P>
 * 16:媒体服务器收到BYE消息后回复200OK响应,会话断开。<br/>
 * 17:SIP服务器向媒体服务器发送BYE消息,断开消息2、3、6建立的同媒体服务器的Invite会话。<br/>
 * </P>
 */
public class MediaByeResponseProcessor1 extends ResponseProcessor {
    @Override
    protected void process(ResponseEvent event, FlowContext context) {
        final ClientTransaction clientTransaction = context.get(PlaySession.SIP_MEDIA_SESSION_1);
        final Request bye = SipRequestFactory.getByeRequest(clientTransaction);
        sendByeRequest(bye, clientTransaction);

        FlowContextCache.setNewKey(getCallId(event), SipRequestFactory.getCallId(bye));
    }
}
