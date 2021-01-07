package com.hlcy.yun.gb28181.sip.message.handler.request;

import com.hlcy.yun.gb28181.sip.message.handler.RequestHandler;
import lombok.extern.slf4j.Slf4j;

import javax.sip.RequestEvent;
import javax.sip.message.Request;

@Slf4j
public class UnsupportedRequestHandler extends RequestHandler {
    @Override
    public void handle(RequestEvent event) {
        log.warn("Receive a unsupported request: {}", event.getRequest());
    }
}
