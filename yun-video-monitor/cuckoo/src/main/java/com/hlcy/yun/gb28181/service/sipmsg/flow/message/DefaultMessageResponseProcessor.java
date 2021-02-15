package com.hlcy.yun.gb28181.service.sipmsg.flow.message;

import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;

import javax.sip.ResponseEvent;

public class DefaultMessageResponseProcessor extends MessageResponseProcessor {
    @Override
    protected void doProcess(ResponseEvent event) {
        final FlowContext context = FlowContextCacheUtil.get(getCallId(event.getResponse()));
        DeferredResultHolder.setDeferredResultForRequest(context.getCallbackKey(), "ok");
    }

    @Override
    public void cleanupContextWhenClientResponseException(ResponseEvent event) {
        final FlowContext context = FlowContextCacheUtil.get(getCallId(event.getResponse()));
        DeferredResultHolder.setErrorDeferredResultForRequest(context.getCallbackKey(), "error");
    }

    @Override
    public void cleanupContext(ResponseEvent event) {
        final FlowContext context = FlowContextCacheUtil.get(getCallId(event.getResponse()));
        FlowContextCacheUtil.remove(getCallId(event.getResponse()));
        context.clearSessionCache();
    }
}
