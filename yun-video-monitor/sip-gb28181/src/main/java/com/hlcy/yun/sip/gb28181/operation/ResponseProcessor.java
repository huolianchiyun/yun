package com.hlcy.yun.sip.gb28181.operation;

import com.hlcy.yun.sip.gb28181.message.MessageHandler;
import com.hlcy.yun.sip.gb28181.operation.flow.FlowContext;
import com.hlcy.yun.sip.gb28181.operation.flow.FlowContextCache;
import javax.sip.ClientTransaction;
import javax.sip.ResponseEvent;
import javax.sip.header.CallIdHeader;

public abstract class ResponseProcessor extends MessageHandler<ResponseEvent> {

    @Override
    public void handle(ResponseEvent event) {
        final FlowContext flowContext = getFlowContext(event);
        process(event, flowContext);
        flowContext.switch2NextProcessor();
    }

    protected abstract void process(ResponseEvent event, FlowContext context);

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

    protected String getResponseBody(ResponseEvent event){
        return new String(event.getResponse().getRawContent());
    }
}
