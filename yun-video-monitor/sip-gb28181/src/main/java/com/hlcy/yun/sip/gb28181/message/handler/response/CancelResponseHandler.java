package com.hlcy.yun.sip.gb28181.message.handler.response;

import com.hlcy.yun.sip.gb28181.message.handler.ResponseHandler;
import lombok.extern.slf4j.Slf4j;

import javax.sip.ResponseEvent;
import javax.sip.message.Response;

@Slf4j
public class CancelResponseHandler extends ResponseHandler {
    public CancelResponseHandler(String name, String method) {
        super(name, method);
    }

    @Override
    protected void doHandle(ResponseEvent event) {
        log.info("Receive a cancel response: {}.", event.getResponse());
        if (event.getResponse().getStatusCode() == Response.OK) {
            getFlowContext(event).getProcessor().handle(event);
        }
    }
}
