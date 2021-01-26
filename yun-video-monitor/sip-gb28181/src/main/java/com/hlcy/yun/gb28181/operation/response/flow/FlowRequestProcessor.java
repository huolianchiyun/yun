package com.hlcy.yun.gb28181.operation.response.flow;

import com.hlcy.yun.gb28181.sip.client.RequestProcessor;

import javax.sip.RequestEvent;

public abstract class FlowRequestProcessor extends RequestProcessor<FlowContext> {

    @Override
    protected FlowContext getContext(RequestEvent event) {
//        return FlowContextCache.get(getCallId(event));
        return null;
    }
}
