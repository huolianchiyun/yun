package com.hlcy.yun.gb28181.service.sipmsg.flow.message;

import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.sip.biz.ResponseProcessor;
import lombok.extern.slf4j.Slf4j;

import javax.sip.ResponseEvent;

@Slf4j
public abstract class MessageResponseProcessor extends ResponseProcessor<FlowContext> {

    @Override
    protected void process(ResponseEvent event, FlowContext context) {
        doProcess(event);
    }

    protected abstract void doProcess(ResponseEvent event);

    @Override
    protected FlowContext getContext(ResponseEvent event) {
        return FlowContextCacheUtil.get(getCallId(event.getResponse()));
    }
}
