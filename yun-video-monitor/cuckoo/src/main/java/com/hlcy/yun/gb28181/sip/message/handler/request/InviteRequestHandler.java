package com.hlcy.yun.gb28181.sip.message.handler.request;

import com.hlcy.yun.gb28181.sip.biz.RequestProcessor;
import com.hlcy.yun.gb28181.sip.message.handler.RequestHandler;
import lombok.extern.slf4j.Slf4j;

import javax.sip.RequestEvent;
import javax.sip.message.Request;

@Slf4j
public class InviteRequestHandler extends RequestHandler {
    @Override
    public void handle(RequestEvent event) {
        if (!Request.INVITE.equals(event.getRequest().getMethod())) {
            this.next.handle(event);
            return;
        }
        final RequestProcessor requestProcessor = processorFactory.getRequestProcessor(event);
        if (requestProcessor != null) {
            requestProcessor.handle(event);
        } else {
            log.warn("A invite request has no corresponding processor, the request will be ignored, request \n{}", event.getRequest());
        }
    }
}
