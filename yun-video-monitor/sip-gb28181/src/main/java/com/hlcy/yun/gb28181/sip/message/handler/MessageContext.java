package com.hlcy.yun.gb28181.sip.message.handler;

import com.hlcy.yun.gb28181.sip.biz.RequestProcessor;
import com.hlcy.yun.gb28181.sip.biz.ResponseProcessor;
import com.hlcy.yun.gb28181.sip.message.Pipeline;

/**
 * MessageContext 用于关联 RequestProcessor 和 ResponseProcessor
 */
public interface MessageContext {

    RequestProcessor requestProcessor();

    ResponseProcessor responseProcessor();

    void switchResponseProcessor2next();

    Pipeline Pipeline(PipelineType type);

    enum PipelineType {
        REQUEST, RESPONSE
    }
}
