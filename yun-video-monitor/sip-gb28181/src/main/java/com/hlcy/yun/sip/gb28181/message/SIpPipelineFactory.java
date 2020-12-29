package com.hlcy.yun.sip.gb28181.message;

import com.hlcy.yun.sip.gb28181.message.handler.RequestHandler;
import com.hlcy.yun.sip.gb28181.message.handler.ResponseHandler;
import com.hlcy.yun.sip.gb28181.message.handler.request.*;
import com.hlcy.yun.sip.gb28181.message.handler.response.InviteResponseHandler;

import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.message.Request;

public class SIpPipelineFactory {
    private final static DefaultPipeline<RequestHandler, RequestEvent> REQUEST_PIPELINE = new DefaultPipeline<>();
    private final static DefaultPipeline<ResponseHandler, ResponseEvent> RESPONSE_PIPELINE = new DefaultPipeline<>();

    static {
        REQUEST_PIPELINE.addLast("register", new RegisterRequestHandler());
        REQUEST_PIPELINE.addLast("invite", new InviteRequestHandler());
        REQUEST_PIPELINE.addLast("ack", new AckRequestHandler());
        REQUEST_PIPELINE.addLast("bye", new ByeRequestHandler());
        REQUEST_PIPELINE.addLast("cancel", new CancelRequestHandler());
        REQUEST_PIPELINE.addLast("message", new MessageRequestHandler());
        REQUEST_PIPELINE.addLast("subscribe", new SubscribeRequestHandler());

        RESPONSE_PIPELINE.addLast("invite", new InviteResponseHandler(Request.INVITE, Request.INVITE));
    }

    public static DefaultPipeline<RequestHandler, RequestEvent> getRequestPipeline() {
        return REQUEST_PIPELINE;
    }

    public static DefaultPipeline<ResponseHandler, ResponseEvent> getResponsePipeline() {
        return RESPONSE_PIPELINE;
    }
}
