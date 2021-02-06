package com.hlcy.yun.gb28181.sip.biz;

import com.hlcy.yun.gb28181.sip.SipLayer;
import com.hlcy.yun.gb28181.sip.message.factory.Transport;
import com.hlcy.yun.gb28181.sip.message.MessageHandler;
import com.hlcy.yun.gb28181.sip.message.handler.MessageContext;
import gov.nist.javax.sip.SipStackImpl;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.stack.SIPServerTransaction;
import lombok.extern.slf4j.Slf4j;

import javax.sip.*;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.text.ParseException;

@Slf4j
public abstract class RequestProcessor<C extends MessageContext> extends MessageHandler<RequestEvent> {

    @Override
    public void handle(RequestEvent event) {
        final C context = getContext(event);
        process(event, context);
    }

    protected abstract void process(RequestEvent event, C context);

    protected abstract C getContext(RequestEvent event);

    /**
     * Send 200 response
     * Creates a new Response message of type specified by the statusCode parameter, based on a specific Request message.
     * This new Response does not contain a body.
     *
     * @param event request event
     * @throws ParseException
     */
    protected void send200Response(RequestEvent event) throws ParseException {
        sendResponse(event, buildResponse(Response.OK, event.getRequest()));
    }

    /**
     * Responding to request event
     *
     * @param event    request event
     * @param response
     */
    protected void sendResponse(RequestEvent event, Response response) {
        try {
            getServerTransaction(event).sendResponse(response);
        } catch (SipException | InvalidArgumentException e) {
            log.error("Responding to request event failed, cause: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private ServerTransaction getServerTransaction(RequestEvent event) {
        Request request = event.getRequest();
        final Transport transport = SipLayer.getTransport(((ViaHeader) request.getHeader(ViaHeader.NAME)).getTransport());
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

    /**
     * Creates a new Response message of type specified by the statusCode
     * parameter, based on a specific Request message. This new Response does
     * not contain a body.
     *
     * @param statusCode -
     *                   the new integer of the statusCode value of this Message.
     * @param request    -
     *                   the received Request object upon which to base the Response.
     * @throws ParseException which signals that an error has been reached unexpectedly
     *                        while parsing the statusCode.
     */
    protected Response buildResponse(int statusCode, Request request) throws ParseException {
        return SipLayer.getMessageFactory().createResponse(statusCode, request);
    }

    protected String getCallId(Request request) {
        return ((CallIdHeader) request.getHeader(CallIdHeader.NAME)).getCallId();
    }

    public RequestProcessor<MessageContext> getNextProcessor() {
        return (RequestProcessor<MessageContext>) next;
    }

}
