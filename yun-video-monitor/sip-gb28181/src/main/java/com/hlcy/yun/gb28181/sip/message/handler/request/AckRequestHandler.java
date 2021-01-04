package com.hlcy.yun.gb28181.sip.message.handler.request;

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
        if (log.isDebugEnabled()) {
            log.debug("Receive a ack request: {}.", event.getRequest());
        }
        Request request = event.getRequest();
        Dialog dialog = event.getDialog();
        try {
            CSeq csReq = (CSeq) request.getHeader(CSeq.NAME);
            Request ackRequest = dialog.createAck(csReq.getSeqNumber());
            dialog.sendAck(ackRequest);
            log.info("send a ack to callee: {}", ackRequest);
        } catch (SipException | InvalidArgumentException e) {
            e.printStackTrace();
        }
    }
}
