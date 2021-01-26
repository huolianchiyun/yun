package com.hlcy.yun.gb28181.sip.message.handler.request;

import com.hlcy.yun.gb28181.operation.response.flow.FlowPipelineFactory;
import com.hlcy.yun.gb28181.operation.response.flow.Operation;
import com.hlcy.yun.gb28181.sip.message.handler.MessageContext;
import com.hlcy.yun.gb28181.sip.message.handler.RequestHandler;
import lombok.extern.slf4j.Slf4j;

import javax.sip.RequestEvent;
import javax.sip.message.Request;

@Slf4j
public class MessageRequestHandler extends RequestHandler {
    @Override
    public void handle(RequestEvent event) {
        if (!Request.MESSAGE.equals(event.getRequest().getMethod())) {
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
            // TODO 优化 jie ou
            FlowPipelineFactory.getRequestFlowPipeline(Operation.KEEPALIVE).first().handle(event);
        }
    }
}
