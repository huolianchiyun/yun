package com.hlcy.yun.sip.gb28181.message;

import com.hlcy.yun.sip.gb28181.message.handler.request.*;

public class PipelineInitializer {
    private final static RequestPipeline requestPipeline = new DefaultRequestPipeline();
    private final static ResponsePipeline responsePipeline = new DefaultResponsePipeline();

    static {
        requestPipeline.addLast("register", new RegisterRequestHandler());
        requestPipeline.addLast("invite", new InviteRequestHandler());
        requestPipeline.addLast("ack", new AckRequestHandler());
        requestPipeline.addLast("bye", new ByeRequestHandler());
        requestPipeline.addLast("cancel", new CancelRequestHandler());
        requestPipeline.addLast("message", new MessageRequestHandler());
        requestPipeline.addLast("subscribe", new SubscribeRequestHandler());
    }

    public static RequestPipeline getRequestPipeline() {
        return requestPipeline;
    }

    public static ResponsePipeline getResponsePipeline() {
        return responsePipeline;
    }
}
