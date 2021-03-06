package com.hlcy.yun.gb28181.sip.message.handler.request;

import com.hlcy.yun.gb28181.sip.message.MessageHandler;
import com.hlcy.yun.gb28181.sip.message.handler.MessageContext;
import com.hlcy.yun.gb28181.sip.message.handler.RequestHandler;
import lombok.extern.slf4j.Slf4j;

import javax.sip.RequestEvent;
import javax.sip.message.Request;

import static com.hlcy.yun.gb28181.sip.message.handler.MessageContext.PipelineType.REQUEST;

@Slf4j
public class ByeRequestHandler extends RequestHandler {
    @Override
    public void handle(RequestEvent event) {
        if (!Request.BYE.equals(event.getRequest().getMethod())) {
            this.next.handle(event);
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("Receive a bye request: \n{}", event.getRequest());
        }

        final MessageContext messageContext = getMessageContext(event);
        if (messageContext != null) {
            final MessageHandler ack = messageContext.pipeline(REQUEST).get(Request.BYE);
            //noinspection unchecked
            ack.handle(event);
        } else {
            log.warn("A bye request has no corresponding processor, the request will be ignored, request \n{}", event.getRequest());
            // TODO 后续添加 cleanup flow context and 若是设备操作 将其情况考虑 setDefferResult, 目前发现海康设备繁忙 直接发bye 而非 486
        }

    }
}
