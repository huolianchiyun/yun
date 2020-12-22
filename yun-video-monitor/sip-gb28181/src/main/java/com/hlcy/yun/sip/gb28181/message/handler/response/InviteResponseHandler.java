package com.hlcy.yun.sip.gb28181.message.handler.response;

import com.hlcy.yun.sip.gb28181.message.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.ResponseEvent;
import javax.sip.SipException;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.text.ParseException;

@Slf4j
public class InviteResponseHandler extends ResponseHandler {
    @Override
    public void handle(ResponseEvent event) {
        if (!Request.INVITE.equals(getMethodFrom(event))) {
            this.next.handle(event);
            return;
        }

        log.info("Receive a invite response: {}.", event.getResponse());
        try {
            Response response = event.getResponse();
            int statusCode = response.getStatusCode();
            // 下发 ack
            if (statusCode == Response.OK) {
                Dialog dialog = event.getDialog();
                CSeqHeader header = (CSeqHeader) response.getHeader(CSeqHeader.NAME);
                Request requestAck = dialog.createAck(header.getSeqNumber());

                SipURI requestURI = (SipURI) requestAck.getRequestURI();
                ViaHeader viaHeader = (ViaHeader) response.getHeader(ViaHeader.NAME);
                requestURI.setHost(viaHeader.getHost());
                requestURI.setPort(viaHeader.getPort());
                requestAck.setRequestURI(requestURI);

                dialog.sendAck(requestAck);
            }
        } catch (InvalidArgumentException | SipException | ParseException e) {
            log.error("Handle a invite response({}) failed, cause: {}.", event, e.getMessage());
            e.printStackTrace();
        }
    }
}
