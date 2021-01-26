package com.hlcy.yun.gb28181.sip.client;

import com.hlcy.yun.gb28181.sip.message.MessageHandler;
import com.hlcy.yun.gb28181.sip.message.handler.MessageContext;
import lombok.extern.slf4j.Slf4j;

import javax.sdp.SdpException;
import javax.sip.ClientTransaction;
import javax.sip.ResponseEvent;
import javax.sip.header.CallIdHeader;
import javax.sip.message.Response;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

@Slf4j
public abstract class ResponseProcessor<C extends MessageContext> extends MessageHandler<ResponseEvent> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public void handle(ResponseEvent event) {
        final int status = event.getResponse().getStatusCode();
        if (!(status == Response.OK)) {
            log.warn("Receive a exception response, status：{}, message: \n{}", status, event.getResponse().getReasonPhrase());
            cleanupContextWhenException(event);
            return;
        }

        final C context = getContext(event);
        try {
            process(event, context);
            context.setCurrentResponseProcessor2next();
            cleanupContext(event);
        } catch (SdpException e) {
            e.printStackTrace();
        }
    }

    public void cleanupContext(ResponseEvent event) {
        // 子类需要时重写实现
    }

    public void cleanupContextWhenException(ResponseEvent event) {
        // 子类需要时重写实现
    }

    protected abstract void process(ResponseEvent event, C context) throws SdpException;

    public ResponseProcessor<MessageContext> getNextProcessor() {
        return (ResponseProcessor<MessageContext>) next;
    }

    protected ClientTransaction getClientTransaction(ResponseEvent event) {
        return event.getClientTransaction();
    }

    protected abstract C getContext(ResponseEvent event);

    protected String getCallId(ResponseEvent event) {
        return ((CallIdHeader) event.getResponse().getHeader(CallIdHeader.NAME)).getCallId();
    }

    protected String getResponseBodyByStr(ResponseEvent event) {
        return new String(event.getResponse().getRawContent());
    }

    protected byte[] getResponseBodyByByteArr(ResponseEvent event) {
        return new String(event.getResponse().getRawContent()).getBytes(StandardCharsets.UTF_8);
    }
}
