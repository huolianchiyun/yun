package com.hlcy.yun.gb28181.operation;

import com.hlcy.yun.gb28181.sip.message.MessageHandler;
import com.hlcy.yun.gb28181.operation.flow.FlowContext;
import com.hlcy.yun.gb28181.operation.flow.FlowContextCache;
import lombok.extern.slf4j.Slf4j;

import javax.sdp.SdpException;
import javax.sip.ClientTransaction;
import javax.sip.ResponseEvent;
import javax.sip.header.CallIdHeader;
import java.nio.charset.StandardCharsets;

public abstract class ResponseProcessor extends MessageHandler<ResponseEvent> {

    @Override
    public void handle(ResponseEvent event) {
        final FlowContext flowContext = getFlowContext(event);
        try {
            process(event, flowContext);
            flowContext.switch2NextProcessor();
        } catch (SdpException e) {
            e.printStackTrace();
        }
    }

    protected abstract void process(ResponseEvent event, FlowContext context) throws SdpException;

    public ResponseProcessor getNextProcessor() {
        return (ResponseProcessor) next;
    }

    protected ClientTransaction getClientTransaction(ResponseEvent event) {
        return event.getClientTransaction();
    }

    protected FlowContext getFlowContext(ResponseEvent event) {
        return FlowContextCache.get(getCallId(event));
    }

    protected String getCallId(ResponseEvent event) {
        return ((CallIdHeader) event.getResponse().getHeader(CallIdHeader.NAME)).getCallId();
    }

    protected String getResponseBody1(ResponseEvent event){
        return new String(event.getResponse().getRawContent());
    }

    protected byte[] getResponseBody2(ResponseEvent event){
        return new String(event.getResponse().getRawContent()).getBytes(StandardCharsets.UTF_8);
    }
}
