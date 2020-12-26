package com.hlcy.yun.sip.gb28181.message.handler.response;

import com.hlcy.yun.sip.gb28181.message.handler.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import javax.sip.ResponseEvent;
import javax.sip.message.Response;

@Slf4j
public class ByeResponseHandler extends ResponseHandler {
    public ByeResponseHandler(String name, String method) {
        super(name, method);
    }

    @Override
    public void doHandle(ResponseEvent event) {
        log.info("Receive a bye response: {}.", event.getResponse());
        if (event.getResponse().getStatusCode() == Response.OK) {
            getFlowContext(event).getProcessor().handle(event);
        }
    }
}
