package com.hlcy.yun.sip.gb28181.message.handler.request;

import com.hlcy.yun.sip.gb28181.message.RequestHandler;
import lombok.extern.slf4j.Slf4j;

import javax.sip.RequestEvent;
import javax.sip.message.Request;

@Slf4j
public class MessageRequestHandler extends RequestHandler {
    @Override
    public void handle(RequestEvent event) {
        if (!Request.MESSAGE.equals(event.getRequest().getMethod())){
            this.next.handle(event);
            return;
        }

        log.info("Receive a subscribe request: {}", event.getRequest());





    }
}
