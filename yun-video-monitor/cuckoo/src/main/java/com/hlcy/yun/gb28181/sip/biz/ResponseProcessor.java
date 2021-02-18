package com.hlcy.yun.gb28181.sip.biz;

import com.hlcy.yun.gb28181.sip.message.MessageHandler;
import com.hlcy.yun.gb28181.sip.message.handler.MessageContext;
import lombok.extern.slf4j.Slf4j;

import javax.sdp.SdpException;
import javax.sip.ClientTransaction;
import javax.sip.ResponseEvent;
import javax.sip.header.CallIdHeader;
import javax.sip.message.Response;
import java.io.Serializable;

@Slf4j
public abstract class ResponseProcessor<C extends MessageContext> extends MessageHandler<ResponseEvent> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public void handle(ResponseEvent event) {
        final int status = event.getResponse().getStatusCode();
        if (!(status == Response.OK)) {
            log.warn("Receive a exception response, status：{}, message: \n{}", status, event.getResponse().getReasonPhrase());
            cleanupContextWhenClientResponseException(event);
            return;
        }

        final C context = getContext(event);
        try {
            process(event, context);
            context.switchResponseProcessor2next();
            cleanupContext(event);
        } catch (SdpException e) {
            e.printStackTrace();
        }
    }

    protected void cleanupContextWhenClientResponseException(ResponseEvent event) {
        // 子类需要时重写实现
    }

    protected abstract void process(ResponseEvent event, C context) throws SdpException;

    protected void cleanupContext(ResponseEvent event) {
        // 子类需要时重写实现
    }

    public ResponseProcessor<MessageContext> getNextProcessor() {
        return (ResponseProcessor<MessageContext>) next;
    }

    protected ClientTransaction getClientTransaction(ResponseEvent event) {
        return event.getClientTransaction();
    }

    protected abstract C getContext(ResponseEvent event);

    protected String getCallId(Response response) {
        return ((CallIdHeader) response.getHeader(CallIdHeader.NAME)).getCallId();
    }
}
