package com.hlcy.yun.gb28181.sip.biz;

import com.hlcy.yun.gb28181.sip.SipLayer;
import gov.nist.javax.sip.SipStackImpl;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.stack.SIPServerTransaction;
import lombok.extern.slf4j.Slf4j;

import javax.sip.*;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

@Slf4j
public final class ResponseSender {

    public static void sendResponse(ServerTransaction serverTransaction, Response response) {
        try {
            serverTransaction.sendResponse(response);
        } catch (SipException | InvalidArgumentException e) {
            log.error("Responding to request event failed, cause: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Responding to request event
     *
     * @param event    request event
     * @param response /
     */
    public static void sendResponse(RequestEvent event, Response response) {
        try {
            getServerTransaction(event).sendResponse(response);
        } catch (SipException | InvalidArgumentException e) {
            log.error("Responding to request event failed, cause: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private static ServerTransaction getServerTransaction(RequestEvent event) {
        Request request = event.getRequest();
        final SipLayer.Transport transport = SipLayer.getTransport(((ViaHeader) request.getHeader(ViaHeader.NAME)).getTransport());
        ServerTransaction serverTransaction = event.getServerTransaction();
        if (serverTransaction == null) {
            final SipProvider sipProvider = SipLayer.getSipProvider(transport);
            SipStackImpl sipStack = (SipStackImpl) sipProvider.getSipStack();
            serverTransaction = (SIPServerTransaction) sipStack.findTransaction((SIPRequest) request, true);
            if (serverTransaction == null) {
                try {
                    serverTransaction = sipProvider.getNewServerTransaction(request);
                } catch (TransactionAlreadyExistsException | TransactionUnavailableException e) {
                    e.printStackTrace();
                }
            }
        }
        return serverTransaction;
    }
}
