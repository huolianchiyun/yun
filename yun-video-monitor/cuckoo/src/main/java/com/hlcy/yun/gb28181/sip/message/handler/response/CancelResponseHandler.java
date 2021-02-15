package com.hlcy.yun.gb28181.sip.message.handler.response;

import com.hlcy.yun.gb28181.sip.message.handler.ResponseHandler;
import lombok.extern.slf4j.Slf4j;

import javax.sip.ResponseEvent;

@Slf4j
public class CancelResponseHandler extends ResponseHandler {
    public CancelResponseHandler(String name, String method) {
        super(name, method);
    }

    @Override
    protected void doHandle(ResponseEvent event) {
        log.info("Receive a cancel response: \n{}", event.getResponse());
        getMessageContext(event).responseProcessor().handle(event);
    }
}
