package com.hlcy.yun.gb28181.sip.message.handler.request;

import com.hlcy.yun.gb28181.sip.client.RequestProcessor;
import com.hlcy.yun.gb28181.sip.message.handler.MessageContext;
import com.hlcy.yun.gb28181.sip.message.handler.RequestHandler;
import lombok.extern.slf4j.Slf4j;
import javax.sip.RequestEvent;
import javax.sip.message.Request;

@Slf4j
public class MessageRequestHandler extends RequestHandler {
    @Override
    public void handle(RequestEvent event) {
        if (!Request.MESSAGE.equals(getMethodFrom(event))) {
            this.next.handle(event);
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("Receive a message request: \n{}", event.getRequest());
        }

        final MessageContext context = getMessageContext(event);
        if (context != null) {
            context.getRequestProcessor().handle(event);
        } else {
            final RequestProcessor requestProcessor = processorFactory.getRequestProcessorBy(event);
            if (requestProcessor != null) {
                requestProcessor.handle(event);
            } else {
                log.warn("A message request is not handled, request \n{}", event.getRequest());
            }
        }
    }
}
