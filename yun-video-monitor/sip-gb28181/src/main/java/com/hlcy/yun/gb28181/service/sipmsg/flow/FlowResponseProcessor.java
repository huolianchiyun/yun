package com.hlcy.yun.gb28181.service.sipmsg.flow;

import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.sip.biz.ResponseProcessor;

import javax.sip.ResponseEvent;
import javax.sip.message.Response;

public abstract class FlowResponseProcessor extends ResponseProcessor<FlowContext> {

    @Override
    public void cleanupContextWhenClientResponseException(ResponseEvent event) {
        final FlowContext flowContext = getContext(event);
        final String ssrc = flowContext.getSsrc();

        // 当 ssrc 未获取，flow 执行异常时，清理 flow context
        if (ssrc == null || ssrc.isEmpty()) {
            String key = null;

            final Operation operation = flowContext.getOperation();
            if (Operation.PLAY == operation) {
                key = DeferredResultHolder.CALLBACK_CMD_PLAY + flowContext.getOperationalParams().getChannelId();
            } else if (Operation.PLAYBACK == operation) {
                key = DeferredResultHolder.CALLBACK_CMD_PLAYBACK + flowContext.getOperationalParams().getChannelId();
            } else if (Operation.BROADCAST == operation) {
                key = DeferredResultHolder.CALLBACK_CMD_VOICE + flowContext.getOperationalParams().getChannelId();
            }

            if (key != null) {
                final Response response = event.getResponse();
                DeferredResultHolder.setErrorDeferredResultForRequest(key,
                        response.getStatusCode() == Response.BUSY_HERE ? "设备繁忙，请稍后再试！" : response.getReasonPhrase());
            }
            flowContext.clearSessionCache();
            FlowContextCacheUtil.remove(getCallId(event.getResponse()));
        }
    }

    @Override
    protected FlowContext getContext(ResponseEvent event) {
        return FlowContextCacheUtil.get(getCallId(event.getResponse()));
    }
}
