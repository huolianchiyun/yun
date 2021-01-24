package com.hlcy.yun.gb28181.operation;

import com.hlcy.yun.gb28181.operation.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.operation.flow.Operation;
import com.hlcy.yun.gb28181.sip.message.MessageHandler;
import com.hlcy.yun.gb28181.operation.flow.FlowContext;
import com.hlcy.yun.gb28181.operation.flow.FlowContextCache;
import lombok.extern.slf4j.Slf4j;

import javax.sdp.SdpException;
import javax.sip.ClientTransaction;
import javax.sip.ResponseEvent;
import javax.sip.header.CallIdHeader;
import javax.sip.message.Response;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

@Slf4j
public abstract class ResponseProcessor extends MessageHandler<ResponseEvent> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public void handle(ResponseEvent event) {
        final int status = event.getResponse().getStatusCode();
        if (!(status == Response.OK)) {
            log.warn("Receive a exception response, status：{}, message: {}.", status, event.getResponse().getReasonPhrase());
            // clean up play flow environment
            cleanupFlowContext(event);
            return;
        }

        final FlowContext flowContext = getFlowContext(event);
        try {
            process(event, flowContext);
            flowContext.switch2NextProcessor();
        } catch (SdpException e) {
            e.printStackTrace();
        }
    }

    private void cleanupFlowContext(ResponseEvent event) {
        final FlowContext flowContext = getFlowContext(event);
        final String ssrc = flowContext.getSsrc();

        if (ssrc == null || ssrc.isEmpty()) {
            String key = null;

            final Operation operation = flowContext.getOperation();
            if (Operation.PLAY == operation) {
                key = DeferredResultHolder.CALLBACK_CMD_PLAY + flowContext.getPlayParams().getChannelId();
            } else if (Operation.PLAYBACK == operation) {
                key = DeferredResultHolder.CALLBACK_CMD_PLAYBACK + flowContext.getPlaybackParams().getChannelId();
            }

            if (key != null) {
                final Response response = event.getResponse();
                DeferredResultHolder.setErrorDeferredResultForRequest(key,
                        response.getStatusCode() == Response.BUSY_HERE ? "设备繁忙，请稍后再试！" : response.getReasonPhrase());
            }
        }
        flowContext.clearSessionCache();
        FlowContextCache.remove(getCallId(event));
    }

    public ResponseProcessor getNextProcessor() {
        return (ResponseProcessor) next;
    }

    protected abstract void process(ResponseEvent event, FlowContext context) throws SdpException;

    protected ClientTransaction getClientTransaction(ResponseEvent event) {
        return event.getClientTransaction();
    }

    protected FlowContext getFlowContext(ResponseEvent event) {
        return FlowContextCache.get(getCallId(event));
    }

    protected String getCallId(ResponseEvent event) {
        return ((CallIdHeader) event.getResponse().getHeader(CallIdHeader.NAME)).getCallId();
    }

    protected String getResponseBody1(ResponseEvent event) {
        return new String(event.getResponse().getRawContent());
    }

    protected byte[] getResponseBody2(ResponseEvent event) {
        return new String(event.getResponse().getRawContent()).getBytes(StandardCharsets.UTF_8);
    }
}
