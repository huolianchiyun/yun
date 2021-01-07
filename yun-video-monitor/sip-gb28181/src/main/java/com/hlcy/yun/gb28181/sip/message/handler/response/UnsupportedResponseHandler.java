package com.hlcy.yun.gb28181.sip.message.handler.response;

import com.hlcy.yun.gb28181.sip.message.handler.ResponseHandler;
import lombok.extern.slf4j.Slf4j;

import javax.sip.ResponseEvent;

@Slf4j
public class UnsupportedResponseHandler extends ResponseHandler {
    public UnsupportedResponseHandler(String name, String method) {
        super(name, method);
    }

    @Override
    protected void doHandle(ResponseEvent event) {
        log.warn("Receive a unsupported response: {}.", event.getResponse());
    }
}
