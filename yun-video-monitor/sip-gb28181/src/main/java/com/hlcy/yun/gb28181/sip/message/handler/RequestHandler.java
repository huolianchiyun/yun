package com.hlcy.yun.gb28181.sip.message.handler;

import com.hlcy.yun.gb28181.sip.SipLayer;
import com.hlcy.yun.gb28181.sip.client.MessageContextCache;
import com.hlcy.yun.gb28181.sip.client.RequestProcessorFactory;
import com.hlcy.yun.gb28181.sip.message.MessageHandler;
import gov.nist.javax.sip.SipStackImpl;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.stack.SIPServerTransaction;
import lombok.extern.slf4j.Slf4j;

import javax.sip.*;
import javax.sip.header.CallIdHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.text.ParseException;

/**
 * @implNote 子类要保证线程安全
 */
@Slf4j
public abstract class RequestHandler extends MessageHandler<RequestEvent> {
    private static MessageContextCache<? extends MessageContext> contextCache;
    protected static RequestProcessorFactory processorFactory;

    protected String name;

    public abstract void handle(RequestEvent event);

    protected MessageContext getMessageContext(RequestEvent event) {
        return contextCache.get(getCallId(event));
    }

    protected String getCallId(RequestEvent event) {
        return ((CallIdHeader) event.getRequest().getHeader(CallIdHeader.NAME)).getCallId();
    }

    /**
     * 从获取请求中方法类型
     * @param event
     * @return Invite, Message, Register and so on
     */
    protected String getMethodFrom(RequestEvent event) {
        return event.getRequest().getMethod();
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

    protected HeaderFactory getHeaderFactory() {
        return SipLayer.getHeaderFactory();
    }

    /**
     * Responding to request event
     *
     * @param event    request event
     * @param response /
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

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static void setContextCache(MessageContextCache<? extends MessageContext> contextCache) {
        RequestHandler.contextCache = contextCache;
    }

    public static void setProcessorFactory(RequestProcessorFactory processorFactory) {
        RequestHandler.processorFactory = processorFactory;
    }
}
