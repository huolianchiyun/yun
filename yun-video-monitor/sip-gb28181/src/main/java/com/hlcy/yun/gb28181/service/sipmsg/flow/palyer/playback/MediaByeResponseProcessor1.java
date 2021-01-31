package com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.playback;

import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowResponseProcessor;
import com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.play.PlaySession;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.sip.biz.RequestSender;
import com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory;

import javax.sip.ClientTransaction;
import javax.sip.ResponseEvent;
import javax.sip.message.Request;

/**
 * 客户端主动发起的历史视音频回放流程: 16->17<br/>
 * <p>
 * 16:媒体服务器收到BYE消息后回复200OK响应,会话断开。<br/>
 * 17:SIP服务器向媒体服务器发送BYE消息,断开消息2、3、6建立的同媒体服务器的Invite会话。<br/>
 * </P>
 */
public class MediaByeResponseProcessor1 extends FlowResponseProcessor {
    @Override
    protected void process(ResponseEvent event, FlowContext context) {
        final ClientTransaction clientTransaction = context.getClientTransaction(PlaySession.SIP_MEDIA_SESSION_1);
        final Request bye = SipRequestFactory.getByeRequest(clientTransaction);
        RequestSender.sendByeRequest(bye, clientTransaction);

        FlowContextCacheUtil.setNewKey(getCallId(event.getResponse()), SipRequestFactory.getCallId(bye));
    }
}
