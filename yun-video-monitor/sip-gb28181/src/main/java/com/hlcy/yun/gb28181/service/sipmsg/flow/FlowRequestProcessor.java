package com.hlcy.yun.gb28181.service.sipmsg.flow;

import com.hlcy.yun.gb28181.sip.biz.RequestProcessor;

import javax.sip.RequestEvent;

public abstract class FlowRequestProcessor extends RequestProcessor<FlowContext> {

    @Override
    protected FlowContext getContext(RequestEvent event) {
        return FlowContextCacheUtil.get(getCallId(event.getRequest()));
    }
}
