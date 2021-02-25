package com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify;

import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowPipelineFactory;
import com.hlcy.yun.gb28181.service.sipmsg.flow.Operation;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.MessageRequestProcessor;
import com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.download.DownloadSession;
import com.hlcy.yun.gb28181.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;

import javax.sip.ClientTransaction;
import javax.sip.RequestEvent;
import javax.sip.message.Request;

import java.util.Optional;

import static com.hlcy.yun.gb28181.service.sipmsg.flow.FlowPipelineFactory.getResponseFlowPipeline;
import static com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify.NotifyType.HISTORY_MEDIA_FILE_SEND_END;
import static com.hlcy.yun.gb28181.sip.biz.RequestSender.sendByeRequest;
import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.getByeRequest;
import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.getCallId;
import static com.hlcy.yun.gb28181.util.XmlUtil.getTextOfChildTagFrom;

@Slf4j
public class MediaStatusNotifyRequestProcessor extends MessageRequestProcessor {

    @Override
    protected void doProcess(RequestEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Receive a CmdType <MediaStatus> request, message: {}.", event.getRequest());
        }
        Element rootElement = getRootElementFrom(event);
        String deviceId = getTextOfChildTagFrom(rootElement, "DeviceID");
        String notifyType = getTextOfChildTagFrom(rootElement, "NotifyType");
        if (HISTORY_MEDIA_FILE_SEND_END.equals(notifyType)) {
            // 可以根据callId


            final Optional<FlowContext> optional = FlowContextCacheUtil.findFlowContextByOperationAndChannelId(Operation.DOWNLOAD, deviceId);
            if (optional.isPresent()) {
                ClientTransaction clientTransaction = null;
                final FlowContext context = optional.get();
                if (Operation.DOWNLOAD == context.getOperation()) {
                    clientTransaction = context.isFromDeserialization()
                            ? context.getClientTransaction(DownloadSession.SIP_DEVICE_SESSION)
                            : context.getClientTransaction(DownloadSession.SIP_MEDIA_SESSION_2);
                }
                if (clientTransaction == null) {
                    log.warn("*** For the end of sending historical file, the clientTransaction of {} has been lost, deviceId: {}", context.getOperation(), deviceId);
                }
                if (context.isFromDeserialization()) {
                    // 将当前响应处理器切换为 device by response
                    context.setCurrentResponseProcessor(getResponseFlowPipeline(context.getOperation()).get("DeviceByResponseProcessor"));
                }
                if (clientTransaction != null) {
                    final Request bye = getByeRequest(clientTransaction);
                    sendByeRequest(bye, clientTransaction);
                    FlowContextCacheUtil.setNewKey(context.getSsrc(), getCallId(bye));
                }
            } else {
                log.warn("*** For the end of sending historical file, the FlowContext has been lost when get clientTransaction, deviceId: {}", deviceId);
            }
        }
    }
}
