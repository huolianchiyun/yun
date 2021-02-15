package com.hlcy.yun.gb28181.sip.message.handler;

import com.hlcy.yun.gb28181.sip.SipLayer;
import com.hlcy.yun.gb28181.sip.biz.RequestProcessorFactory;
import com.hlcy.yun.gb28181.sip.biz.ResponseSender;
import com.hlcy.yun.gb28181.sip.message.MessageHandler;
import com.hlcy.yun.gb28181.sip.message.factory.SipResponseFactory;
import lombok.extern.slf4j.Slf4j;
import javax.sip.*;
import javax.sip.header.CallIdHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

/**
 * @implNote 子类要保证线程安全
 */
@Slf4j
public abstract class RequestHandler extends MessageHandler<RequestEvent> {
    protected static RequestProcessorFactory processorFactory;
    private static MessageContextManager contextManager;

    protected String name;

    public abstract void handle(RequestEvent event);

    protected MessageContext getMessageContext(RequestEvent event) {
        return contextManager.get(event.getRequest());
    }

    protected String getCallId(RequestEvent event) {
        return ((CallIdHeader) event.getRequest().getHeader(CallIdHeader.NAME)).getCallId();
    }

    /**
     * 从获取请求中方法类型
     *
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
     */
    protected Response buildResponse(int statusCode, Request request) {
        return SipResponseFactory.buildResponse(statusCode, request);
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
        ResponseSender.sendResponse(event, response);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static void setProcessorFactory(RequestProcessorFactory processorFactory) {
        RequestHandler.processorFactory = processorFactory;
    }

    public static void setContextManager(MessageContextManager contextManager) {
        RequestHandler.contextManager = contextManager;
    }
}
