package com.hlcy.yun.gb28181.sip.message.handler.request;

import com.hlcy.yun.gb28181.sip.message.handler.MessageContext;
import com.hlcy.yun.gb28181.sip.message.handler.RequestHandler;
import javax.sip.RequestEvent;
import javax.sip.message.Request;

public class InviteRequestHandler extends RequestHandler {
    @Override
    public void handle(RequestEvent event) {
        if (!Request.INVITE.equals(event.getRequest().getMethod())) {
            this.next.handle(event);
            return;
        }
        MessageContext messageContext = getMessageContext(event);
        if (messageContext != null) {
            messageContext.getRequestProcessor().handle(event);
        }
    }
}
