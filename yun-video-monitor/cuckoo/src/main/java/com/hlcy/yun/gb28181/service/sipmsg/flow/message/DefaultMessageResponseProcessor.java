package com.hlcy.yun.gb28181.service.sipmsg.flow.message;

import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.sip.ResponseEvent;

@Setter
@Accessors(chain = true)
public class DefaultMessageResponseProcessor extends MessageResponseProcessor {
    boolean isResponseCallback = true;

    @Override
    protected void doProcess(ResponseEvent event) {
        final FlowContext context = FlowContextCacheUtil.get(getCallId(event.getResponse()));
        if (context != null && isResponseCallback) {
            DeferredResultHolder.setDeferredResultForRequest(context.getOperationalParams().getCallbackKey(), "ok");
        }
    }

    @Override
    protected void cleanupContextWhenClientResponseException(ResponseEvent event) {
        final FlowContext context = FlowContextCacheUtil.get(getCallId(event.getResponse()));
        DeferredResultHolder.setErrorDeferredResultForRequest(context.getOperationalParams().getCallbackKey(), "error");
    }

    @Override
    protected void cleanupContext(ResponseEvent event) {
        final FlowContext context = FlowContextCacheUtil.get(getCallId(event.getResponse()));
        FlowContextCacheUtil.remove(getCallId(event.getResponse()));
        context.clearSessionCache();
    }
}
