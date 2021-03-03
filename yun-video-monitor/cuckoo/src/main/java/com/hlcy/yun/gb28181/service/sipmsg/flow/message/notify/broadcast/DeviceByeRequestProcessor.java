package com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify.broadcast;

import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowRequestProcessor;
import com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory;

import javax.sip.ClientTransaction;
import javax.sip.RequestEvent;
import javax.sip.message.Request;

import static com.hlcy.yun.gb28181.sip.biz.RequestSender.sendByeRequest;

/**
 * 处理设备主动发 bye
 */
public class DeviceByeRequestProcessor extends FlowRequestProcessor {

    @Override
    protected void process(RequestEvent event, FlowContext context) {
        send200Response(event);

        final ClientTransaction clientTransaction = context.getClientTransaction(VoiceSession.SIP_MEDIA_SESSION);
        final Request bye = SipRequestFactory.getByeRequest(clientTransaction);
        sendByeRequest(bye, clientTransaction);

        // 清除以 VoiceBroadcastNotifyCmd.stop(ssrc) 方式关流预留的资源
        FlowContextCacheUtil.remove(context.getSsrc());
        FlowContextCacheUtil.setNewKey(getCallId(event.getRequest()), SipRequestFactory.getCallId(bye));
    }
}
