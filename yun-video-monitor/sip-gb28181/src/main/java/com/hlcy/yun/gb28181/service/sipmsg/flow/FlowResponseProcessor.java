package com.hlcy.yun.gb28181.service.sipmsg.flow;

import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.sip.client.ResponseProcessor;
import javax.sip.ResponseEvent;
import javax.sip.message.Response;

public abstract class FlowResponseProcessor extends ResponseProcessor<FlowContext> {

    @Override
    public void cleanupContextWhenException(ResponseEvent event) {
        final FlowContext flowContext = getContext(event);
        final String ssrc = flowContext.getSsrc();

        if (ssrc == null || ssrc.isEmpty()) {
            String key = null;

            final Operation operation = flowContext.getOperation();
            if (Operation.PLAY == operation) {
                key = DeferredResultHolder.CALLBACK_CMD_PLAY + flowContext.getOperationalParams().getChannelId();
            } else if (Operation.PLAYBACK == operation) {
                key = DeferredResultHolder.CALLBACK_CMD_PLAYBACK + flowContext.getOperationalParams().getChannelId();
            }

            if (key != null) {
                final Response response = event.getResponse();
                DeferredResultHolder.setErrorDeferredResultForRequest(key,
                        response.getStatusCode() == Response.BUSY_HERE ? "设备繁忙，请稍后再试！" : response.getReasonPhrase());
            }
        }
        flowContext.clearSessionCache();
        FlowContextCacheUtil.remove(getCallId(event));
    }

    @Override
    protected FlowContext getContext(ResponseEvent event) {
        return FlowContextCacheUtil.get(getCallId(event));
    }
}
