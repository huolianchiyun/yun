package com.hlcy.yun.gb28181.operation;

import com.hlcy.yun.gb28181.sip.message.MessageHandler;

import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;

public abstract class RequestProcessor extends MessageHandler<RequestEvent> {

    @Override
    public void handle(RequestEvent event) {
        process(event);
    }

    protected abstract void process(RequestEvent event);

    protected ServerTransaction getClientTransaction(RequestEvent event) {
        return event.getServerTransaction();
    }
}
