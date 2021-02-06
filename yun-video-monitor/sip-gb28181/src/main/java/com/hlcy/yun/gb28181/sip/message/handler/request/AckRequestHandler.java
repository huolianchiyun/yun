package com.hlcy.yun.gb28181.sip.message.handler.request;

import com.hlcy.yun.gb28181.sip.message.handler.MessageContext;
import com.hlcy.yun.gb28181.sip.message.handler.RequestHandler;
import gov.nist.javax.sip.header.CSeq;
import lombok.extern.slf4j.Slf4j;

import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.SipException;
import javax.sip.message.Request;

@Slf4j
public class AckRequestHandler extends RequestHandler {
    @Override
    public void handle(RequestEvent event) {
        if (!Request.ACK.equals(event.getRequest().getMethod())) {
            this.next.handle(event);
            return;
        }
        log.info("Receive a ack request: \n{}", event.getRequest());
        final MessageContext messageContext = getMessageContext(event);
        if (messageContext != null) {
            messageContext.requestProcessor().handle(event);
        }
    }
}
