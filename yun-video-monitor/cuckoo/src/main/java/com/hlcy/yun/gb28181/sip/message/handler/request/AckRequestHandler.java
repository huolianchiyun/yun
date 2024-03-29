package com.hlcy.yun.gb28181.sip.message.handler.request;

import com.hlcy.yun.gb28181.sip.message.MessageHandler;
import com.hlcy.yun.gb28181.sip.message.handler.MessageContext;
import com.hlcy.yun.gb28181.sip.message.handler.RequestHandler;
import lombok.extern.slf4j.Slf4j;

import javax.sip.RequestEvent;
import javax.sip.message.Request;


import static com.hlcy.yun.gb28181.sip.message.handler.MessageContext.PipelineType.REQUEST;

@Slf4j
public class AckRequestHandler extends RequestHandler {
    @Override
    public void handle(RequestEvent event) {
        if (!Request.ACK.equals(event.getRequest().getMethod())) {
            this.next.handle(event);
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("Receive a ack request: \n{}", event.getRequest());
        }

        final MessageContext messageContext = getMessageContext(event);
        if (messageContext != null) {
            final MessageHandler ack = messageContext.pipeline(REQUEST).get(Request.ACK);
            //noinspection unchecked
            ack.handle(event);
        } else {
            log.warn("A ack request has no corresponding processor, the request will be ignored, request \n{}", event.getRequest());
        }
    }
}
