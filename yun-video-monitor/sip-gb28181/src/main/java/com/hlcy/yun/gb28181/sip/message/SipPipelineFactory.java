package com.hlcy.yun.gb28181.sip.message;

import com.hlcy.yun.gb28181.sip.message.handler.RequestHandler;
import com.hlcy.yun.gb28181.sip.message.handler.ResponseHandler;
import com.hlcy.yun.gb28181.sip.message.handler.request.*;
import com.hlcy.yun.gb28181.sip.message.handler.response.ByeResponseHandler;
import com.hlcy.yun.gb28181.sip.message.handler.response.CancelResponseHandler;
import com.hlcy.yun.gb28181.sip.message.handler.response.InviteResponseHandler;
import com.hlcy.yun.gb28181.sip.message.handler.response.UnsupportedResponseHandler;

import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.message.Request;

public class SipPipelineFactory {
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
        REQUEST_PIPELINE.addLast("unsupported", new UnsupportedRequestHandler());

        RESPONSE_PIPELINE.addLast("invite", new InviteResponseHandler(Request.INVITE, Request.INVITE));
        RESPONSE_PIPELINE.addLast("cancel", new CancelResponseHandler(Request.CANCEL, Request.CANCEL));
        RESPONSE_PIPELINE.addLast("bye", new ByeResponseHandler(Request.BYE, Request.BYE));
        // TODO 优化
        RESPONSE_PIPELINE.addLast("unsupported", new UnsupportedResponseHandler("unsupported", "unsupported"));
    }

    public static DefaultPipeline<RequestHandler, RequestEvent> getRequestPipeline() {
        return REQUEST_PIPELINE;
    }

    public static DefaultPipeline<ResponseHandler, ResponseEvent> getResponsePipeline() {
        return RESPONSE_PIPELINE;
    }
}
