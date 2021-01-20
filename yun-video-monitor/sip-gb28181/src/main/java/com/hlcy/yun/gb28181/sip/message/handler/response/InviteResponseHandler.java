package com.hlcy.yun.gb28181.sip.message.handler.response;

import com.hlcy.yun.gb28181.sip.message.handler.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import javax.sip.ResponseEvent;
import javax.sip.message.Response;

@Slf4j
public class InviteResponseHandler extends ResponseHandler {
    public InviteResponseHandler(String name, String method) {
        super(name, method);
    }

    @Override
    public void doHandle(ResponseEvent event) {
        log.info("Receive a invite response: \n{}", event.getResponse());
        final int status = event.getResponse().getStatusCode();
//        if (status == Response.OK || status == Response.BUSY_HERE) {
            getFlowContext(event).getProcessor().handle(event);
//        }
    }
}
